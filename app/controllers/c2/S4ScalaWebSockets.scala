package controllers.c2

import controllers.Assets
import play.api.mvc._
import play.api.Logger

/**
 * Created by trydofor on 7/5/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaWebSockets
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/async/code/ScalaWebSockets.scala
 *
 */

class S4ScalaWebSockets extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c2/2-4-a1",
      "/c2/2-4-a2",
      "/c2/2-4-a2?f=true",
      "/c2/2-4-a3",
      "/c2/2-4-a4",
      "/c2/2-4-a5",
      "/c2/2-4-a6",
      "/c2/2-4-a7",
      "/c2/2-4-ax",
      "/c2/2-4-ws"
    )

    Ok(views.html.list("2.4.WebSockets", list))
  }

  // actors

  import play.api.mvc._
  import play.api.Play.current

  def a1 = Action {
    Ok(views.html.c2.s4("/c2/2-4-a1ws", "message"))
  }

  import akka.actor._

  object MyWebSocketActor {
    def props(out: ActorRef) = Props(new MyWebSocketActor(out))
  }

  class MyWebSocketActor(out: ActorRef) extends Actor {
    Logger.info("open")

    import akka.actor.PoisonPill

    def receive = {
      case "PoisonPill" =>
        Logger.info("I received PoisonPill")
        self ! PoisonPill
      case msg: String =>
        Logger.info("I received your message: " + msg)
        out ! ("I received your message: " + msg)
    }

    override def postStop() = {
      Logger.info("stop")
      //      someResource.close()
    }
  }

  def a1ws = WebSocket.acceptWithActor[String, String] { request => out =>
    MyWebSocketActor.props(out)
  }


  // Rejecting a WebSocket
  def a2(flag: Boolean) = Action {
    if (flag)
      Ok(views.html.c2.s4("/c2/2-4-a2ws", "PoisonPill")).withSession("user" -> "trydofor")
    else
      Ok(views.html.c2.s4("/c2/2-4-a2ws", "PoisonPill")).withNewSession
  }

  import scala.concurrent.Future
  import play.api.mvc._
  import play.api.Play.current

  def a2ws = WebSocket.tryAcceptWithActor[String, String] { request =>
    Future.successful(request.session.get("user") match {
      case None => Left(Forbidden)
      case Some(_) => Right(MyWebSocketActor.props)
    })
  }

  // Handling different types of messages
  def a3 = Action {
    Ok(views.html.c2.s4("/c2/2-4-a3ws","""{"key":"value"}"""))
  }

  import play.api.mvc._
  import play.api.libs.json._
  import play.api.Play.current

  class MyWebSocketActorJson(out: ActorRef) extends Actor {

    import play.api.libs.json.JsValue

    def receive = {
      case msg: JsValue =>
        Logger.info("I received your message: " + msg)
        out ! msg
    }
  }

  object MyWebSocketActorJson {
    def props(out: ActorRef) = Props(new MyWebSocketActorJson(out))
  }

  def a3ws = WebSocket.acceptWithActor[JsValue, JsValue] { request => out =>
    MyWebSocketActorJson.props(out)
  }


  // json - class
  def a4 = Action {
    Ok(views.html.c2.s4("/c2/2-4-a4ws","""{"foo":"must contain `foo` key","other-key":1}"""))
  }

  case class InEvent(foo: String)
  case class OutEvent(bar: String)

  class MyWebSocketActorBean(out: ActorRef) extends Actor {
    def receive = {
      case InEvent(foo) =>
        out ! OutEvent(foo)
    }
  }

  object MyWebSocketActorBean {
    def props(out: ActorRef) = Props(new MyWebSocketActorBean(out))
  }

  //#actor-json-formats
  import play.api.libs.json._

  implicit val inEventFormat = Json.format[InEvent]
  implicit val outEventFormat = Json.format[OutEvent]
  //#actor-json-formats

  //#actor-json-frames
  import play.api.mvc.WebSocket.FrameFormatter

  implicit val inEventFrameFormatter = FrameFormatter.jsonFrame[InEvent]
  implicit val outEventFrameFormatter = FrameFormatter.jsonFrame[OutEvent]
  //#actor-json-frames

  //#actor-json-in-out
  import play.api.mvc._
  import play.api.Play.current

  def a4ws = WebSocket.acceptWithActor[InEvent, OutEvent] { request => out =>
    MyWebSocketActorBean.props(out)
  }


  // Handling WebSockets with iteratees
  def a5 = Action {
    Ok(views.html.c2.s4("/c2/2-4-a5ws","""iteratee"""))
  }
  import play.api.mvc._
  import play.api.libs.iteratee._
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def a5ws = WebSocket.using[String] { request =>

    // Log events to the console
    val in = Iteratee.foreach[String](println).map { _ =>
      println("Disconnected")
    }

    // Send a single 'Hello!' message
    val out = Enumerator("Hello!")

    (in, out)
  }


  def a6 = Action {
    Ok(views.html.c2.s4("/c2/2-4-a6ws","""iteratee2"""))
  }
  import play.api.mvc._
  import play.api.libs.iteratee._

  def a6ws = WebSocket.using[String] { request =>

    // Just ignore the input
    val in = Iteratee.ignore[String]

    // Send a single 'Hello!' message and close
    val out = Enumerator("Hello!").andThen(Enumerator.eof)

    (in, out)
  }

  def a7 = Action {
    Ok(views.html.c2.s4("/c2/2-4-a7ws","""iteratee3"""))
  }
  import play.api.mvc._
  import play.api.libs.iteratee._
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def a7ws =  WebSocket.using[String] { request =>

    // Concurrent.broadcast returns (Enumerator, Concurrent.Channel)
    val (out, channel) = Concurrent.broadcast[String]

    // log the message to stdout and send response back to client
    val in = Iteratee.foreach[String] {
      msg => println(msg)
        // the Enumerator returned by Concurrent.broadcast subscribes to the channel and will
        // receive the pushed messages
        channel push("I received your message: " + msg)
    }
    (in,out)
  }

  // same as  `/c2/2-4-ws`
  val ax = Assets.at(path = "/public/html/", file = "web-socket-test.html")
}

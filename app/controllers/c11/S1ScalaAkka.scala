package controllers.c11

/**
 * Created by trydofor on 7/14/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaAkka
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/akka/code/ScalaAkka.scala
 * @see conf/application.conf
 */

import akka.actor._

object HelloActor {
  def props = Props[HelloActor]

  case class SayHello(name: String)

}

class HelloActor extends Actor {

  import HelloActor._

  def receive = {
    case SayHello(name: String) =>
      import play.api.Logger
      Logger.info(s"HelloActor $name")
      sender() ! "Hello, " + name
  }
}

import akka.actor._
import javax.inject._
import play.api.Configuration

object ConfiguredActor {

  case object GetConfig

}

class ConfiguredActor @Inject()(configuration: Configuration) extends Actor {

  import ConfiguredActor._

  val config = configuration.getString("my.config").getOrElse("none")

  def receive = {
    case GetConfig =>
      sender() ! config
  }
}

import play.api.mvc._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import javax.inject._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class Application @Inject()(@Named("configured-actor") configuredActor: ActorRef)
                           (implicit ec: ExecutionContext) extends Controller {

  implicit val timeout: Timeout = 5.seconds

  def getConfig = Action.async {
    import ConfiguredActor._
    (configuredActor ? GetConfig).mapTo[String].map { message =>
      Ok(message)
    }
  }
}


@Singleton
class S1ScalaAkka @Inject()(system: ActorSystem) extends Controller {

  val a0 = Action {
    Ok( """
      11.Integrating with Akka

      all codes work well
      just ctrl-c and ctrl-v
        """)
  }
  //...

  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  import scala.concurrent.duration._
  import akka.pattern.ask
  import HelloActor._

  implicit val timeout = akka.util.Timeout(5 seconds)

  val helloActor = system.actorOf(HelloActor.props, "hello-actor")

  def sayHello(name: String) = Action.async {
    (helloActor ? SayHello(name)).mapTo[String].map { message =>
      Ok(message)
    }
  }

  import com.google.inject.AbstractModule
  import play.api.libs.concurrent.AkkaGuiceSupport

  class MyModule extends AbstractModule with AkkaGuiceSupport {
    def configure = {
      bindActor[ConfiguredActor]("configured-actor")
    }
  }

  import akka.actor._
  import javax.inject._
  import com.google.inject.assistedinject.Assisted
  import play.api.Configuration

  object ConfiguredChildActor {

    case object GetConfig

    trait Factory {
      def apply(key: String): Actor
    }

  }

  class ConfiguredChildActor @Inject()(configuration: Configuration,
                                       @Assisted key: String) extends Actor {

    import ConfiguredChildActor._

    val config = configuration.getString(key).getOrElse("none")

    def receive = {
      case GetConfig =>
        sender() ! config
    }
  }

  import akka.actor._
  import javax.inject._
  import play.api.libs.concurrent.InjectedActorSupport

  object ParentActor {

    case class GetChild(key: String)

  }

  class ParentActor @Inject()(
                               childFactory: ConfiguredChildActor.Factory
                               ) extends Actor with InjectedActorSupport {

    import ParentActor._

    def receive = {
      case GetChild(key: String) =>
        val child: ActorRef = injectedChild(childFactory(key), key)
        sender() ! child
    }
  }

  import com.google.inject.AbstractModule
  import play.api.libs.concurrent.AkkaGuiceSupport

  class MyModule2 extends AbstractModule with AkkaGuiceSupport {
    def configure = {
      bindActor[ParentActor]("parent-actor")
      bindActorFactory[ConfiguredChildActor, ConfiguredChildActor.Factory]
    }
  }

  import scala.concurrent.duration._

  val cancellable = system.scheduler.schedule(
    0.microseconds, 300.microseconds, helloActor, SayHello("tick"))



  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  system.scheduler.scheduleOnce(1.seconds) {
    //    file.delete()
    cancellable.cancel()
  }
}

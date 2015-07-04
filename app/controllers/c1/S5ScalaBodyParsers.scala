package controllers.c1

import java.io.File

import play.api.mvc._

/**
 * Created by trydofor on 7/2/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaBodyParsers
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/http/code/ScalaBodyParsers.scala
 */

class S5ScalaBodyParsers extends Controller {

  trait Action[A] extends (Request[A] => Result) {
    def parser: BodyParser[A]
  }

  trait Request[+A] extends RequestHeader {
    def body: A
  }

  /////////////

  def a0 = Action {
    Ok(views.html.c1.s5()).withSession("username" -> "trydofor")
  }

  def a1 = Action { request =>
    val body: AnyContent = request.body
    val textBody: Option[String] = body.asText

    // Expecting text body
    textBody.map { text =>
      Ok("Got: " + text)
    }.getOrElse {
      BadRequest("Expecting text/plain request body")
    }
  }

  // import play.api.mvc.BodyParsers.parse
  def a2 = Action(parse.text) { request =>
    Ok("Got: " + request.body)
  }

  def a3 = Action(parse.tolerantText) { request =>
    Ok("Got: " + request.body)
  }

  val folder = "./target/"

  def a4 = Action(parse.file(to = new File(folder + "upload"))) { request =>
    Ok("Saved the request content to " + request.body)
  }


  val storeInUserFile = parse.using { request =>
    request.session.get("username").map { user =>
      parse.file(to = new File(folder + "upload." + user))
    }.getOrElse {
      sys.error("You don't have the right to upload here")
    }
  }

  def a5 = Action(storeInUserFile) { request =>
    Ok("Saved the request content to " + request.body)
  }

  // Accept only 10B of data.
  def a6 = Action(parse.text(maxLength = 10)) { request =>
    Ok("Got: " + request.body)
  }

  // Accept only 10B of data.
  def a7 = Action(parse.maxLength(10, storeInUserFile)) { request =>
    Ok("Saved the request content to " + request.body)
  }
}

package controllers.c1

import play.api.Logger
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future

/**
 * Created by trydofor on 7/2/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaContentNegotiation
 */

class S7ScalaContentNegotiation extends Controller {

  def a0 = Action {
    Ok(views.html.c1.s7()).withSession("username" -> "trydofor")
  }

  object Item {
    def findAll = List(1, 2, 3)
  }

  val a1 = Action { implicit request =>
    val items = Item.findAll
    render {
      case Accepts.Html() => Ok(<b>items</b>).as(HTML)
      case Accepts.Json() => Ok(Json.toJson(items))
    }
  }

  val a2 = Action { implicit request =>

    def ??? = Ok("ok")
    //#extract_custom_accept_type
    val AcceptsMp3 = Accepting("audio/mp3")
    render {
      case AcceptsMp3() => ???
    }
  }
}

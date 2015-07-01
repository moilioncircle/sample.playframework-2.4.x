package controllers.c1

import play.api.libs.iteratee.Enumerator
import play.api.mvc._

/**
 * Created by trydofor on 7/1/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaActions
 */

class S1ScalaAction extends Controller {

  // Building an Action
  val a1 = Action {
    Ok("Hello world")
  }

  val a2 = Action { request =>
    Ok("Got request [" + request + "]")
  }

  val a3 = Action { implicit request =>
    Ok("Got request [" + request + "]")
  }

  //Controllers are action generators
  def a4 = Action {
    Ok("It works!")
  }

  def a5(number: Long) = Action {
    Ok("Hello " + number)
  }

  // Simple results

  def a6 = Action {
    Result(
      header = ResponseHeader(200, Map(CONTENT_TYPE -> "text/plain")),
      body = Enumerator("Hello world!".getBytes())
    )
  }

  def a7(name: String) = Action {

    val result: Result = name match {
      case "ok" => Ok("Hello world!")
      case "notFound" => NotFound
      case "pageNotFound" => NotFound(<h1>Page not found</h1>)
      //case "badRequest" => BadRequest(views.html.form(formWithErrors))
      case "oops" => InternalServerError("Oops")
      case "anyStatus" => Status(488)("Strange response type")
      case _ => Ok("Hello world!")
    }
    result
  }

  // Redirects are simple results too
  def a8 = Action {
    Redirect("http://www.moilioncircle.com", MOVED_PERMANENTLY)
  }

  def a9(name: String) = TODO
}

package controllers.c1

import play.api.libs.iteratee.Enumerator
import play.api.mvc._

/**
 * Created by trydofor on 7/1/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaRouting
 */

class S2ScalaRouting extends Controller {

  def a1(id: Long) = Action {
    Ok("show id: " + id)
  }

  def a2 = a1 _

  def a3(id: Long) = Action {
    Ok("show id: " + id)
  }

  def a4(version: Option[String]) = Action {
    Ok("show version: " + version)
  }

  // Redirect to /hello/Bob
  def a5 = Action {
    Redirect(routes.S2ScalaRouting.a4(Some("from a5")))
  }

  def a6 = {
    println("some code here")
    a4(Some("from a6"))
  }
}

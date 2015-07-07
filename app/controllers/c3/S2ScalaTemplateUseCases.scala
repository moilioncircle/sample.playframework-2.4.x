package controllers.c3

import controllers.Assets
import play.api.Logger
import play.api.mvc._

/**
 * Created by trydofor on 7/7/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaTemplateUseCases
 */

class S2ScalaTemplateUseCases extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c3/3-2-a1",
      "/c3/3-2-a2?i=1",
      "/c3/3-2-a2?i=2",
      "/c3/3-2-a3?i=1",
      "/c3/3-2-a3?i=2",
      "/c3/3-2-a4?i=1",
      "/c3/3-2-a4?i=2"
    )

    Ok(views.html.list("3.2.Common use cases", list))
  }

  val a1 = Action {
    Ok(views.html.c3.s2a1t1())
  }

  def a2(i: Int) = Action {
    i match {
      case 1 => Ok(views.html.c3.s2a2t1())
      case _ => Ok(views.html.c3.s2a2t2())
    }
  }

  def a3(i: Int) = Action {
    i match {
      case 1 => Ok(views.html.c3.s2a3t1())
      case _ => Ok(views.html.c3.s2a3t2())
    }
  }

  def a4(i: Int) = Action {
    i match {
      case 1 => Ok(views.html.c3.s2a4t1())
      case _ => Ok(views.html.c3.s2a4t2())
    }
  }
}

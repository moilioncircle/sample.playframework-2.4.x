package controllers.c4

import play.api.mvc._

import scala.concurrent.Future

/**
 * Created by trydofor on 7/8/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaCsrf
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/forms/code/ScalaCsrf.scala
 * @see /build.sbt filters
 * @see /conf/application.conf play.http.filters
 */

import play.api.http.HttpFilters
import play.filters.csrf.CSRFFilter
import javax.inject.Inject

class Filters @Inject() (csrfFilter: CSRFFilter) extends HttpFilters {
  def filters = Seq(csrfFilter)
}

class S2ScalaCsrf extends Controller {

  val a0 = Action { implicit request =>
    Ok(views.html.c4.s2())
  }

  import play.api.mvc._
  import play.filters.csrf._

  def a1 = CSRFCheck {
    Action { implicit req =>
      Ok(views.html.c4.s2())
    }
  }

  def a2 = CSRFAddToken {
    Action { implicit req =>
      Ok(views.html.c4.s2())
    }
  }

  object PostAction extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      // authentication code here
      block(request)
    }
    override def composeAction[A](action: Action[A]) = CSRFCheck(action)
  }

  object GetAction extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      // authentication code here
      block(request)
    }
    override def composeAction[A](action: Action[A]) = CSRFAddToken(action)
  }

  def a3 = PostAction { implicit req =>
    Ok(views.html.c4.s2())
  }

  def a4 = GetAction { implicit req =>
    Ok(views.html.c4.s2())
  }

}

package controllers.c10


/**
 * Created by trydofor on 7/12/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaOpenID
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/ws/code/ScalaOpenIdSpec.scala
 */

import javax.inject.Inject

import play.api.data._
import play.api.Logger
import play.api.data.Forms._
import play.api.libs.openid._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class S2ScalaOpenID @Inject()(openIdClient: OpenIdClient) extends Controller {

  val a0 = Action {
    Ok( """
      10.2.Connecting to OpenID services

      all codes work well
      just ctrl-c and ctrl-v
        """)
  }

  def login = Action {
    Ok("views.html.login()")
  }

  def loginPost = Action.async { implicit request =>
    Form(single(
      "openid" -> nonEmptyText
    )).bindFromRequest.fold({ error =>
      Logger.info("bad request " + error.toString)
      Future.successful(BadRequest(error.toString))
    }, { openId =>
      openIdClient.redirectURL(openId, routes.S2ScalaOpenID.openIdCallback.absoluteURL())
        .map(url => Redirect(url))
        .recover { case t: Throwable => Redirect(routes.S2ScalaOpenID.login)}
    })
  }

  def openIdCallback = Action.async { implicit request =>
    openIdClient.verifiedId(request).map(info => Ok(info.id + "\n" + info.attributes))
      .recover {
      case t: Throwable =>
        // Here you should look at the error, and give feedback to the user
        Redirect(routes.S2ScalaOpenID.login)
    }
  }
}

package controllers.c10

import play.api.libs.oauth._
import play.api.libs.ws._
import play.api.mvc._
import scala.concurrent._


/**
 * Created by trydofor on 7/12/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaOAuth
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/ws/code/ScalaOpenIdSpec.scala
 */

class S3ScalaOAuth extends Controller {

  val a0 = Action {
    Ok("""
      10.3.Accessing resources protected by OAuth

      all codes work well
      just ctrl-c and ctrl-v
       """)
  }

}

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Twitter extends Controller {

  val KEY = ConsumerKey("xxxxx", "xxxxx")

  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    true)

  def authenticate = Action { request =>
    request.getQueryString("oauth_verifier").map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => {
          // We received the authorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.S3ScalaOAuth.a0).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      }
    }.getOrElse(
        TWITTER.retrieveRequestToken("http://localhost:9000/auth") match {
          case Right(t) => {
            // We received the unauthorized tokens in the OAuth object - store it before we proceed
            Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
          }
          case Left(e) => throw e
        })
  }

  def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }
}

object Application extends Controller {

  def timeline = Action.async { implicit request =>
    Twitter.sessionTokenPair match {
      case Some(credentials) => {
        WS.url("https://api.twitter.com/1.1/statuses/home_timeline.json")
          .sign(OAuthCalculator(Twitter.KEY, credentials))
          .get
          .map(result => Ok(result.json))
      }
      case _ => Future.successful(Redirect(routes.S3ScalaOAuth.a0))
    }
  }
}
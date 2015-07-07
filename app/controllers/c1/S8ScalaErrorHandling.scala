package controllers.c1

import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent._

/**
 * Created by trydofor on 7/3/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaErrorHandling
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/http/code/ScalaErrorHandling.scala
 * @see /conf/application.conf play.http.errorHandler
 */

class S8ScalaErrorHandling extends HttpErrorHandler {

  val a0 = Action {
    Ok(views.html.c1.s8())
  }

  val a1 = Action {
    throw new RuntimeException("trydofor")
    Ok("ok")
  }

  val a2 = Action {
    Forbidden("Forbidden")
  }

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      Status(statusCode)(
        s"""
           |A client error occurred: $message
           |-----
           |to debug, comment the line in /conf/application.conf, like this
           |#play.http.errorHandler = "controllers.c1.S8ScalaErrorHandling"
           |""".stripMargin)
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError(
        s"""
            |A server error occurred: ${exception.getMessage}
            |-----
            |to debug, comment the line in /conf/application.conf, like this
            |#play.http.errorHandler = "controllers.c1.S8ScalaErrorHandling"
         """.stripMargin)
    )
  }
}

import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router
import scala.concurrent._

class S8ScalaErrorHandling2 @Inject() (
                               env: Environment,
                               config: Configuration,
                               sourceMapper: OptionalSourceMapper,
                               router: Provider[Router]
                               ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }

  override def onNotFound(request: RequestHeader, message: String) = {
    Future.successful(
      Forbidden("NotFound is Forbidden")
    )
  }
}
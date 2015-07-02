package controllers.c1

import java.io.File

import play.api.mvc._
import play.api.Logger
import scala.concurrent.Future

/**
 * Created by trydofor on 7/2/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaBodyParsers
 */

class S6ScalaActionsComposition extends Controller {

  def a0 = Action {
    Ok(views.html.c1.s5()).withSession("username" -> "trydofor")
  }

  object LoggingAction extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      Logger.info("Calling action")
      block(request)
    }
  }

  def a1 = LoggingAction {
    Ok("Hello World")
  }

  def a2 = LoggingAction(parse.text) { request =>
    Ok("Got a body " + request.body.length + " bytes long")
  }

  case class Logging[A](action: Action[A]) extends Action[A] {

    def apply(request: Request[A]): Future[Result] = {
      Logger.info("Calling action")
      action(request)
    }

    lazy val parser = action.parser
  }

  def a3[A](action: Action[A]) = Action.async(action.parser) { request =>
    Logger.info("Calling action")
    action(request)
  }

  object LoggingAction2 extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      block(request)
    }

    override def composeAction[A](action: Action[A]) = new Logging(action)
  }

  def a4 = LoggingAction2 {
    Ok("Hello World")
  }

  def a5 = Logging {
    Action {
      Ok("Hello World")
    }
  }

  def xForwardedFor[A](action: Action[A]) = Action.async(action.parser) { request =>
    val newRequest = request.headers.get("X-Forwarded-For").map { xff =>
      new WrappedRequest[A](request) {
        override def remoteAddress = xff
      }
    } getOrElse request
    action(newRequest)
  }

  def onlyHttps[A](action: Action[A]) = Action.async(action.parser) { request =>
    request.headers.get("X-Forwarded-Proto").collect {
      case "https" => action(request)
    } getOrElse {
      Future.successful(Forbidden("Only HTTPS requests allowed"))
    }
  }

  import play.api.mvc._
  import play.api.libs.concurrent.Execution.Implicits._

  def addUaHeader[A](action: Action[A]) = Action.async(action.parser) { request =>
    action(request).map(_.withHeaders("X-UA-Compatible" -> "Chrome=1"))
  }

  class UserRequest[A](val username: Option[String], request: Request[A]) extends WrappedRequest[A](request)

  object UserAction extends
  ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
    def transform[A](request: Request[A]) = Future.successful {
      new UserRequest(request.session.get("username"), request)
    }
  }

  // mock class
  case class Item(id: String) {
    def addTag(tag: String) = ()
    def accessibleByUser(user: Option[String]) = user.isDefined
  }
  object ItemDao {
    def findById(id: String) = Some(Item(id))
  }

  class ItemRequest[A](val item: Item, request: UserRequest[A]) extends WrappedRequest[A](request) {
    def username = request.username
  }
  // end mock


  def ItemAction(itemId: String) = new ActionRefiner[UserRequest, ItemRequest] {
    def refine[A](input: UserRequest[A]) = Future.successful {
      ItemDao.findById(itemId)
        .map(new ItemRequest(_, input))
        .toRight(NotFound)
    }
  }

  object PermissionCheckAction extends ActionFilter[ItemRequest] {
    def filter[A](input: ItemRequest[A]) = Future.successful {
      if (!input.item.accessibleByUser(input.username))
        Some(Forbidden)
      else
        None
    }
  }

  def tagItem(itemId: String, tag: String) =
    (UserAction andThen ItemAction(itemId) andThen PermissionCheckAction) { request =>
      request.item.addTag(tag)
      Ok("User " + request.username + " tagged " + request.item.id)
    }
}

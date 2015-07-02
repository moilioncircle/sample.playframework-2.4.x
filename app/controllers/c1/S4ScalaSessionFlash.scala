package controllers.c1

import play.api.mvc._

/**
 * Created by trydofor on 7/1/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaSessionFlash
 */

class S4ScalaSessionFlash extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c1/1-4-a1",
      "/c1/1-4-a2",
      "/c1/1-4-a3",
      "/c1/1-4-a4",
      "/c1/1-4-a5",
      "/c1/1-4-a6",
      "/c1/1-4-a7",
      "/c1/1-4-a8"
    )

    Ok(views.html.list("1.4.Session and Flash scopes", list))
  }

  val a1 = Action {
    Ok("use `F12` to debug network! cookies/PLAY_SESSION").withSession(
      "connected" -> "trydofor@gmail.com")
  }

  val a2 = Action { request =>
    Ok("use `F12` to debug network! cookies/PLAY_SESSION").withSession(
      request.session + ("saidHello" -> "yes"))
  }

  val a3 = Action { request =>
    request.session.get("connected").map { user =>
      Ok("Hello " + user)
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }

  val a4 = Action {
    Ok("Bye").withNewSession
  }

  val a5 = Action { request =>
    Ok {
      request.flash.get("success").getOrElse("Welcome!")
    }
  }

  val a6 = Action { request =>
    Redirect("/c1/1-4-a5").flashing(
      "success" -> "come from a6")
  }

  val a7 = Action { implicit request =>
    Ok(views.html.c1.s4())
  }

  val a8 = Action { request =>
    Redirect(routes.S4ScalaSessionFlash.a7()).flashing(
      "success" -> "come from a8")
  }
}

package controllers.c4

import play.api.data.Form
import play.api.data.Forms._
import play.api.http.HttpVerbs
import play.api.mvc._

/**
 * Created by trydofor on 7/9/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaCustomFieldConstructors
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/forms/code/ScalaFieldConstructor.scala
 */

object MyHelpers {
  implicit val myFields = views.html.helper.FieldConstructor(views.html.c4.s4field.f)
}

class S4ScalaCustomFieldConstructors extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c4/4-4-a1",
      "/c4/4-4-a2"
    )

    Ok(views.html.list("4.3.Custom Validations", list))
  }

  val passwordForm = Form(
    single(
      "password" -> text
    )
  )

  import play.api.Play.current
  import play.api.i18n.Messages.Implicits._
  val a1 = Action { implicit request =>
    if (request.method == HttpVerbs.GET) {
      Ok(views.html.c4.s4a1(passwordForm)) // a1
    } else {
      passwordForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.c4.s4a1(formWithErrors)) // a1
        },
        userData => {
          Redirect(routes.S1ScalaForms.af).flashing("success" -> userData.toString)
        }
      )
    }
  }

  val a2 = Action { implicit request =>
    if (request.method == HttpVerbs.GET) {
      Ok(views.html.c4.s4a2(passwordForm)) // a2
    } else {
      passwordForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.c4.s4a2(formWithErrors)) // a2
        },
        userData => {
          Redirect(routes.S1ScalaForms.af).flashing("success" -> userData.toString)
        }
      )
    }
  }
}

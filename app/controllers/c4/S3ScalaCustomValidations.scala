package controllers.c4

import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.{Invalid, Valid, ValidationError, Constraint}
import play.api.http.HttpVerbs
import play.api.mvc._

/**
 * Created by trydofor on 7/9/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaCustomValidations
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/forms/code/CustomValidations.scala
 */

class S3ScalaCustomValidations extends Controller {

  val allNumbers = """\d*""".r
  val allLetters = """[A-Za-z]*""".r

  val passwordCheckConstraint: Constraint[String] = Constraint("constraints.passwordcheck")({
    plainText =>
      val errors = plainText match {
        case allNumbers() => Seq(ValidationError("Password is all numbers"))
        case allLetters() => Seq(ValidationError("Password is all letters"))
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })

  val passwordCheck: Mapping[String] = nonEmptyText(minLength = 10)
    .verifying(passwordCheckConstraint)

  val passwordForm = Form(
    single(
      "password" -> passwordCheck
    )
  )


  import play.api.Play.current
  import play.api.i18n.Messages.Implicits._
  val a0 = Action { implicit request =>
    if (request.method == HttpVerbs.GET) {
      Ok(views.html.c4.s3(passwordForm))
    } else {
      passwordForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.c4.s3(formWithErrors))
        },
        userData => {
          Redirect(routes.S1ScalaForms.af).flashing("success" -> userData.toString)
        }
      )
    }
  }
}

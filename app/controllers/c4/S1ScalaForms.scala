package controllers.c4

import play.api.http.HttpVerbs
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import play.api.Play.current
import play.api.i18n.Messages.Implicits._

/**
 * Created by trydofor on 7/7/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaTemplateUseCases
 */

case class UserData(name: String, age: Int)
case class User(name: String, age: Int)
case class AddressData(street: String, city: String)
case class UserAddressData(name: String, address: AddressData)
case class UserListData(name: String, emails: List[String])
case class UserOptionalData(name: String, email: Option[String])

// a7
case class Contact(firstname: String,
                   lastname: String,
                   company: Option[String],
                   informations: Seq[ContactInformation])

object Contact {
  def save(contact: Contact): Int = 99
}

case class ContactInformation(label: String,
                              email: Option[String],
                              phones: List[String])

object User {
  def create(u: User) = 9
}

class S1ScalaForms extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c4/4-1-a1",
      "/c4/4-1-a4",
      "/c4/4-1-a5",
      "/c4/4-1-a6",
      "/c4/4-1-a7"
    )

    Ok(views.html.list("4.1.Handling form submission", list))
  }

  val userForm = Form (
    mapping (
      "name" -> text,
      "age" -> number
    ) (UserData.apply) (UserData.unapply)
  )

  val userFormConstraints2 = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> number(min = 0, max = 100)
    )(UserData.apply)(UserData.unapply)
  )

  val boundForm = userFormConstraints2.bind(Map("bob" -> "", "age" -> "25"))
  boundForm.hasErrors

  import play.api.data.validation.Constraints._
  val userFormConstraints = Form(
    mapping(
      "name" -> text.verifying(nonEmpty),
      "age" -> number.verifying(min(0), max(100))
    )(UserData.apply)(UserData.unapply)
  )

  def validate(name: String, age: Int) = {
    name match {
      case "bob" if age >= 18 =>
        Some(UserData(name, age))
      case "admin" =>
        Some(UserData(name, age))
      case _ =>
        None
    }
  }

  val userFormConstraintsAdHoc = Form(
    mapping(
      "name" -> text,
      "age" -> number
    )(UserData.apply)(UserData.unapply) verifying("Failed form constraints!", fields => fields match {
      case userData => validate(userData.name, userData.age).isDefined
    })
  )

  def a1(i: Int) = Action { implicit request =>
    if (i == 1) {
      val anyData = Map("name" -> "bob", "age" -> "17")
      val userData1 = userForm.bind(anyData).get
      Ok(views.html.c4.s1a123(userForm.fill(userData1)))
    } else {
      userFormConstraintsAdHoc.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.c4.s1a123(formWithErrors))
        },
        userData => {
          Redirect(routes.S1ScalaForms.af).flashing("success" -> userData.toString)
        }
      )
    }
  }

  val a2 = Action(parse.form(userForm)) { implicit request =>
    val userData = request.body
    val newUser = User(userData.name, userData.age)
    val id = User.create(newUser)
    Redirect(routes.S1ScalaForms.af).flashing("success" -> s"create new user [id = $id]")
  }

  val a3 = Action(parse.form(userForm,
    onErrors = (formWithErrors: Form[UserData]) => BadRequest(views.html.c4.s1a123(formWithErrors))))
  { implicit request =>
    val userData = request.body
    val newUser = User(userData.name, userData.age)
    val id = User.create(newUser)
    Redirect(routes.S1ScalaForms.af).flashing("success" -> s"create new user [id = $id]")
  }

  val userFormTuple = Form(
    tuple(
      "name" -> text,
      "age" -> number
    ) // tuples come with built-in apply/unapply
  )

  val anyData = Map("name" -> "bob", "age" -> "25")
  val (name, age) = userFormTuple.bind(anyData).get

  val singleForm = Form(
    single(
      "email" -> email
    )
  )

  val emailValue = singleForm.bind(Map("email" -> "bob@example.com")).get

  val filledForm = userForm.fill(UserData("Bob", 18))



  val userFormNested: Form[UserAddressData] = Form(
    mapping(
      "name" -> nonEmptyText,
      "address" -> mapping(
        "street" -> nonEmptyText,
        "city" -> nonEmptyText
      )(AddressData.apply)(AddressData.unapply)
    )(UserAddressData.apply)(UserAddressData.unapply)
  )

  val a4 = Action { implicit request =>

    if (request.method == HttpVerbs.GET) {
      Ok(views.html.c4.s1a4(userFormNested))
    } else {
      userFormNested.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.c4.s1a4(formWithErrors))
        },
        userData => {
          Redirect(routes.S1ScalaForms.af).flashing("success" -> userData.toString)
        }
      )
    }
  }

  val userFormRepeated = Form(
    mapping(
      "name" -> text,
      "emails" -> list(email)
    )(UserListData.apply)(UserListData.unapply)
  )

  val a5 = Action { implicit request =>

    if (request.method == HttpVerbs.GET) {
      Ok(views.html.c4.s1a5(userFormRepeated))
    } else {
      userFormRepeated.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.c4.s1a5(formWithErrors))
        },
        userData => {
          Redirect(routes.S1ScalaForms.af).flashing("success" -> userData.toString)
        }
      )
    }
  }


  val userFormOptional = Form(
    mapping(
      "name" -> text,
      "email" -> optional(email)
    )(UserOptionalData.apply)(UserOptionalData.unapply)
  )

  val userFormDefault = Form(
    mapping(
      "name" -> default(text, "Bob"),
      "age" -> default(number, 18)
    )(User.apply)(User.unapply)
  )

  val userFormStatic = Form(
    mapping(
      "name" -> ignored("a9"),
      "email" -> optional(email)
    )(UserOptionalData.apply)(UserOptionalData.unapply)
  )

  val a6 = Action { implicit request =>

    if (request.method == HttpVerbs.GET) {
      Ok(views.html.c4.s1a6(userFormStatic))
    } else {
      userFormStatic.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.c4.s1a6(formWithErrors))
        },
        userData => {
          Redirect(routes.S1ScalaForms.af).flashing("success" -> userData.toString)
        }
      )
    }
  }

  val contactForm: Form[Contact] = Form(

    // Defines a mapping that will handle Contact values
    mapping(
      "firstname" -> nonEmptyText,
      "lastname" -> nonEmptyText,
      "company" -> optional(text),

      // Defines a repeated mapping
      "informations" -> seq(
        mapping(
          "label" -> nonEmptyText,
          "email" -> optional(email),
          "phones" -> list(
            text verifying pattern("""[0-9.+]+""".r, error="A valid phone number is required")
          )
        )(ContactInformation.apply)(ContactInformation.unapply)
      )
    )(Contact.apply)(Contact.unapply)
  )

  def a7 = Action {
    val existingContact = Contact(
      "Fake", "Contact", Some("Fake company"), informations = List(
        ContactInformation(
          "Personal", Some("fakecontact@gmail.com"), List("01.23.45.67.89", "98.76.54.32.10")
        ),
        ContactInformation(
          "Professional", Some("fakecontact@company.com"), List("01.23.45.67.89")
        ),
        ContactInformation(
          "Previous", Some("fakecontact@oldcompany.com"), List()
        )
      )
    )
    Ok(views.html.c4.s1a7(contactForm.fill(existingContact)))
  }

  def a7s = Action { implicit request =>
    contactForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.c4.s1a7(formWithErrors))
      },
      contact => {
        val contactId = Contact.save(contact)
        Redirect(routes.S1ScalaForms.af).flashing("success" -> contact.toString)
      }
    )
  }

  val af = Action { implicit request =>
    Ok(views.html.c1.s4())
  }
}


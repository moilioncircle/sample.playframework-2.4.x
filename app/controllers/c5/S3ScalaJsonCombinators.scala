package controllers.c5

import play.api.mvc._

/**
 * Created by trydofor on 7/9/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaJsonCombinators
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/json/code/ScalaJsonCombinatorsSpec.scala
 */


class S3ScalaJsonCombinators extends Controller {

  val a0 = Action {
    Ok(
      """
      5.3.JSON Reads/Writes/Format Combinators

      all codes work well
      just ctrl-c and ctrl-v
       """)
  }

  case class Location(lat: Double, long: Double)
  case class Resident(name: String, age: Int, role: Option[String])
  case class Place(name: String, location: Location, residents: Seq[Resident])

  import play.api.libs.json._

  val json: JsValue = Json.parse(
    """
      {
        "name" : "Watership Down",
        "location" : {
          "lat" : 51.235685,
          "long" : -1.309197
        },
        "residents" : [ {
          "name" : "Fiver",
          "age" : 4,
          "role" : null
        }, {
          "name" : "Bigwig",
          "age" : 6,
          "role" : "Owsla"
        } ]
      }
      """)

  // Simple path
  val latPath = JsPath \ "location" \ "lat"

  // Recursive path
  val namesPath = JsPath \\ "name"

  // Indexed path
  val firstResidentPath = (JsPath \ "residents")(0)

  //  __  alias for JsPath
  val longPath = __ \ "location" \ "long"


  import play.

  api.libs.json._

  // JSON library
  import play.api.

  libs.json.Reads._

  // Custom validation helpers
  import play.api.libs.

  functional.syntax._ // Combinator syntax

  val nameReads: Reads[String] = (JsPath \ "name").read[String]

  val locationReadsBuilder =
    (JsPath \ "lat").read[Double] and
      (JsPath \ "long").read[Double]

  implicit val locationReads = locationReadsBuilder.apply(Location.apply _)

  object locationReads2 {
    implicit val locationReads: Reads[Location] = (
      (JsPath \ "lat").read[Double] and
        (JsPath \ "long").read[Double]
      )(Location.apply _)
  }

  val nameReads2: Reads[String] = (JsPath \ "name").read[String]

  val nameResult: JsResult[String] = json.validate[String](nameReads2)

  nameResult match {
    case s: JsSuccess[String] => println("Name: " + s.get)
    case e: JsError => println("Errors: " + JsError.toJson(e).toString())
  }

  val improvedNameReads =
    (JsPath \ "name").read[String](minLength[String](2))

  object together {

    import play.api.libs.json._
    import play.api.libs.json.Reads._
    import play.api.libs.functional.syntax._

    implicit val locationReads: Reads[Location] = (
      (JsPath \ "lat").read[Double](min(-90.0) keepAnd max(90.0)) and
        (JsPath \ "long").read[Double](min(-180.0) keepAnd max(180.0))
      )(Location.apply _)

    implicit val residentReads: Reads[Resident] = (
      (JsPath \ "name").read[String](minLength[String](2)) and
        (JsPath \ "age").read[Int](min(0) keepAnd max(150)) and
        (JsPath \ "role").readNullable[String]
      )(Resident.apply _)

    implicit val placeReads: Reads[Place] = (
      (JsPath \ "name").read[String](minLength[String](2)) and
        (JsPath \ "location").read[Location] and
        (JsPath \ "residents").read[Seq[Resident]]
      )(Place.apply _)


    json.validate[Place] match {
      case s: JsSuccess[Place] => {
        val place: Place = s.get
        // do something with place
      }
      case e: JsError => {
        // error handling flow
      }
    }
  }

  object writes{
    import play.api.libs.json._
    import play.api.libs.functional.syntax._

    implicit val locationWrites: Writes[Location] = (
      (JsPath \ "lat").write[Double] and
        (JsPath \ "long").write[Double]
      )(unlift(Location.unapply))

    implicit val residentWrites: Writes[Resident] = (
      (JsPath \ "name").write[String] and
        (JsPath \ "age").write[Int] and
        (JsPath \ "role").writeNullable[String]
      )(unlift(Resident.unapply))

    implicit val placeWrites: Writes[Place] = (
      (JsPath \ "name").write[String] and
        (JsPath \ "location").write[Location] and
        (JsPath \ "residents").write[Seq[Resident]]
      )(unlift(Place.unapply))


    val place = Place(
      "Watership Down",
      Location(51.235685, -1.309197),
      Seq(
        Resident("Fiver", 4, None),
        Resident("Bigwig", 6, Some("Owsla"))
      )
    )

    val json = Json.toJson(place)
  }

  object format {

    case class User(name: String, friends: Seq[User])

    implicit lazy val userReads: Reads[User] = (
      (__ \ "name").read[String] and
        (__ \ "friends").lazyRead(Reads.seq[User](userReads))
      )(User)

    implicit lazy val userWrites: Writes[User] = (
      (__ \ "name").write[String] and
        (__ \ "friends").lazyWrite(Writes.seq[User](userWrites))
      )(unlift(User.unapply))


    val locationReads: Reads[Location] = (
      (JsPath \ "lat").read[Double](min(-90.0) keepAnd max(90.0)) and
        (JsPath \ "long").read[Double](min(-180.0) keepAnd max(180.0))
      )(Location.apply _)

    val locationWrites: Writes[Location] = (
      (JsPath \ "lat").write[Double] and
        (JsPath \ "long").write[Double]
      )(unlift(Location.unapply))

    implicit val locationFormat: Format[Location] = Format(locationReads, locationWrites)

    object combinators{
      implicit val locationFormat: Format[Location] = (
        (JsPath \ "lat").format[Double](min(-90.0) keepAnd max(90.0)) and
          (JsPath \ "long").format[Double](min(-180.0) keepAnd max(180.0))
        )(Location.apply, unlift(Location.unapply))
    }
  }
}

package controllers.c5

import play.api.mvc._
import play.api.libs.json._

/**
 * Created by trydofor on 7/9/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaJson
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/json/code/ScalaJsonSpec.scala
 */

class S1ScalaJson extends Controller {

  val a0 = Action {
    Ok("""
      5.1.JSON basics

      all codes work well
      just ctrl-c and ctrl-v
       """)
  }


  //Using string parsing
  val json1: JsValue = Json.parse( """
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

  // Using class construction
  val json2: JsValue = JsObject(Seq(
    "name" -> JsString("Watership Down"),
    "location" -> JsObject(Seq("lat" -> JsNumber(51.235685), "long" -> JsNumber(-1.309197))),
    "residents" -> JsArray(Seq(
      JsObject(Seq(
        "name" -> JsString("Fiver"),
        "age" -> JsNumber(4),
        "role" -> JsNull
      )),
      JsObject(Seq(
        "name" -> JsString("Bigwig"),
        "age" -> JsNumber(6),
        "role" -> JsString("Owsla")
      ))
    ))
  ))

  // simplify construction
  val json3: JsValue = Json.obj(
    "name" -> "Watership Down",
    "location" -> Json.obj("lat" -> 51.235685, "long" -> -1.309197),
    "residents" -> Json.arr(
      Json.obj(
        "name" -> "Fiver",
        "age" -> 4,
        "role" -> JsNull
      ),
      Json.obj(
        "name" -> "Bigwig",
        "age" -> 6,
        "role" -> "Owsla"
      )
    )
  )

  // basic types
  val jsonString = Json.toJson("Fiver")
  val jsonNumber = Json.toJson(4)
  val jsonBoolean = Json.toJson(false)

  // collections of basic types
  val jsonArrayOfInts = Json.toJson(Seq(1, 2, 3, 4))
  val jsonArrayOfStrings = Json.toJson(List("Fiver", "Bigwig"))

  case class Location(lat: Double, long: Double)
  case class Resident(name: String, age: Int, role: Option[String])
  case class Place(name: String, location: Location, residents: Seq[Resident])


  implicit val locationWrites = new Writes[Location] {
    def writes(location: Location) = Json.obj(
      "lat" -> location.lat,
      "long" -> location.long
    )
  }

  implicit val residentWrites = new Writes[Resident] {
    def writes(resident: Resident) = Json.obj(
      "name" -> resident.name,
      "age" -> resident.age,
      "role" -> resident.role
    )
  }

  implicit val placeWrites = new Writes[Place] {
    def writes(place: Place) = Json.obj(
      "name" -> place.name,
      "location" -> place.location,
      "residents" -> place.residents)
  }

  // Alternatively, you can define your Writes using the combinator pattern
  object combinator {

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
  }

  val place = Place(
    "Watership Down",
    Location(51.235685, -1.309197),
    Seq(
      Resident("Fiver", 4, None),
      Resident("Bigwig", 6, Some("Owsla"))
    )
  )

  val json = Json.toJson(place)

  val lat = (json \ "location" \ "lat").get
  // returns JsNumber(51.235685)

  val names = json \\ "name"
  // returns Seq(JsString("Watership Down"), JsString("Fiver"), JsString("Bigwig"))

  val bigwig = (json \ "residents")(1)
  // returns {"name":"Bigwig","age":6,"role":"Owsla"}

  val minifiedString: String = Json.stringify(json)
  //{"name":"Watership Down","location":{"lat":51.235685,"long":-1.309197},"residents":[{"name":"Fiver","age":4,"role":null},{"name":"Bigwig","age":6,"role":"Owsla"}]}

  val readableString: String = Json.prettyPrint(json)
  /*
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
  */

  val n1 = (json \ "name").as[String]
  // "Watership Down"

  val n2 = (json \\ "name").map(_.as[String])
  // Seq("Watership Down", "Fiver", "Bigwig")

  val n3 = (json \ "name").asOpt[String]
  // Some("Watership Down")

  val n4 = (json \ "bogus").asOpt[String]
  // None

  val nameResult: JsResult[String] = (json \ "name").validate[String]

  // Pattern matching
  nameResult match {
    case s: JsSuccess[String] => println("Name: " + s.get)
    case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString())
  }

  // Fallback value
  val nameOrFallback = nameResult.getOrElse("Undefined")

  // map
  val nameUpperResult: JsResult[String] = nameResult.map(_.toUpperCase())

  // fold
  val nameOption: Option[String] = nameResult.fold(
    invalid = {
      fieldErrors => fieldErrors.foreach(x => {
        println("field: " + x._1 + ", errors: " + x._2)
      })
        None
    },
    valid = {
      name => Some(name)
    }
  )

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
      (JsPath \ "long").read[Double]
    )(Location.apply _)

  implicit val residentReads: Reads[Resident] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "age").read[Int] and
      (JsPath \ "role").readNullable[String]
    )(Resident.apply _)

  implicit val placeReads: Reads[Place] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "location").read[Location] and
      (JsPath \ "residents").read[Seq[Resident]]
    )(Place.apply _)


  val placeResult: JsResult[Place] = json.validate[Place]
  // JsSuccess(Place(...),)

  val residentResult: JsResult[Resident] = (json \ "residents")(1).validate[Resident]
  // JsSuccess(Resident(Bigwig,6,Some(Owsla)),)
}

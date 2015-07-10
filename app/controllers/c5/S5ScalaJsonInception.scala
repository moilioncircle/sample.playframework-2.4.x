package controllers.c5

import play.api.mvc._

/**
 * Created by trydofor on 7/10/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaJsonInception
 */


class S5ScalaJsonInception extends Controller {

  val a0 = Action {
    Ok(
      """
      5.5.JSON Macro Inception

      all codes work well
      just ctrl-c and ctrl-v
      """)
  }

  object boring {

    import play.api.libs.json._
    import play.api.libs.functional.syntax._

    case class Person(name: String, age: Int, lovesChocolate: Boolean)

    implicit val personReads = (
        (__ \ 'name).read[String] and
        (__ \ 'age).read[Int] and
        (__ \ 'lovesChocolate).read[Boolean]
      )(Person)
  }

  object injectionRw{
    import play.api.libs.json._

    case class Person(name: String, age: Int)

    object Person{
      implicit val personWrites = Json.writes[Person]
      implicit val personReads = Json.reads[Person]
    }

    val json = Json.parse("""{"name":"trydofor",age:36}""")
    val person = Person("trydofor",36)

    val v2 = Json.fromJson[Person](json).get
    val v3 = Json.toJson(person)

    assert(person == v2)
    assert(json == v3)

  }

  object injectionFormat{
    import play.api.libs.json._

    case class Person(name: String, age: Int)

    object Person{
      implicit val personFmt = Json.format[Person]
    }

    val json = Json.parse("""{"name":"trydofor",age:36}""")
    val person = Person("trydofor",36)

    val v2 = Json.fromJson[Person](json).get
    val v3 = Json.toJson(person)

    assert(person == v2)
    assert(json == v3)

  }
}

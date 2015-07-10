package controllers.c5

import play.api.libs.json._
import play.api.mvc._

/**
 * Created by trydofor on 7/10/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaJsonTransformers
 */


class S4ScalaJsonTransformers extends Controller {

  val a0 = Action {
    Ok(
      """
      5.4.JSON Transformers

      all codes work well
      just ctrl-c and ctrl-v
      """)
  }

  val json: JsValue = Json.parse(
    """
      |{
      |    "key1" : "value1",
      |    "key2" : {
      |      "key21" : 123,
      |      "key22" : true,
      |      "key23" : [ "alpha", "beta", "gamma"],
      |      "key24" : {
      |         "key241" : 234.123,
      |         "key242" : "value242"
      |       }
      |    },
      |    "key3" : 234
      |}
    """.stripMargin)


  object case1 {
    val v1 = (__ \ 'key2 \ 'key23).json.pick
    json.transform(v1)

    val v2 = (__ \ 'key2 \ 'key23).json.pick[JsArray]
    json.transform(v2)
  }

  object case2 {
    val jsonTransformer = (__ \ 'key2 \ 'key24 \ 'key241).json.pickBranch
    json.transform(jsonTransformer)
  }

  object case3 {
    val jsonTransformer = (__ \ 'key25 \ 'key251).json.copyFrom((__ \ 'key2 \ 'key21).json.pick)
    json.transform(jsonTransformer)
  }

  object case4 {
    val jsonTransformer = (__ \ 'key2 \ 'key24).json.update(
      __.read[JsObject].map { o => o ++ Json.obj("field243" -> "coucou") }
    )
    json.transform(jsonTransformer)
  }

  object case5 {
    val jsonTransformer = (__ \ 'key24 \ 'key241).json.put(JsNumber(456))
    json.transform(jsonTransformer)
  }

  object case6 {
    val jsonTransformer = (__ \ 'key2 \ 'key22).json.prune
    json.transform(jsonTransformer)
  }

  object case7 {

    import play.api.libs.json._
    import play.api.libs.json.Reads._

    val jsonTransformer = (__ \ 'key2).json.pickBranch(
      (__ \ 'key21).json.update(
        of[JsNumber].map { case JsNumber(nb) => JsNumber(nb + 10) }
      ) andThen
        (__ \ 'key23).json.update(
          of[JsArray].map { case JsArray(arr) => JsArray(arr :+ JsString("delta")) }
        )
    )

    json.transform(jsonTransformer)
  }

  object case8 {

    import play.api.libs.json._

    val jsonTransformer = (__ \ 'key2).json.pickBranch(
      (__ \ 'key23).json.prune
    )

    json.transform(jsonTransformer)
  }

  object combinators {

    val gizmo = Json.obj(
      "name" -> "gizmo",
      "description" -> Json.obj(
        "features" -> Json.arr( "hairy", "cute", "gentle"),
        "size" -> 10,
        "sex" -> "undefined",
        "life_expectancy" -> "very old",
        "danger" -> Json.obj(
          "wet" -> "multiplies",
          "feed after midnight" -> "becomes gremlin"
        )
      ),
      "loves" -> "all"
    )

    val gremlin = Json.obj(
      "name" -> "gremlin",
      "description" -> Json.obj(
        "features" -> Json.arr("skinny", "ugly", "evil"),
        "size" -> 30,
        "sex" -> "undefined",
        "life_expectancy" -> "very old",
        "danger" -> "always"
      ),
      "hates" -> "all"
    )

    import play.api.libs.json._
    import play.api.libs.json.Reads._
    import play.api.libs.functional.syntax._

    val gizmo2gremlin = (
      (__ \ 'name).json.put(JsString("gremlin")) and
        (__ \ 'description).json.pickBranch(
          (__ \ 'size).json.update( of[JsNumber].map{ case JsNumber(size) => JsNumber(size * 3) } ) and
            (__ \ 'features).json.put( Json.arr("skinny", "ugly", "evil") ) and
            (__ \ 'danger).json.put(JsString("always"))
            reduce
        ) and
        (__ \ 'hates).json.copyFrom( (__ \ 'loves).json.pick )
      ) reduce

    gizmo.transform(gizmo2gremlin)
  }

}

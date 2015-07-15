package controllers

import play.api.mvc._

case class Link(section: String, local: String, thisGit: String, playGit: String, playDoc: String)

case class Chapter(title: String, sections: Seq[Link])

class Application extends Controller {

  def index = Action {

    val table = Seq(
      "Main concepts" -> Seq(
        Chapter("1.HTTP programming", Seq(
          Link("1.1.Actions, Controllers and Results", "/c1/1-1",
            "app/controllers/c1/S1ScalaAction.scala",
            "main/http/code/ScalaActions.scala",
            "ScalaActions"),
          Link("1.2.HTTP Routing", "/c1/1-2",
            "app/controllers/c1/S2ScalaRouting.scala",
            "main/http/code/ScalaRouting.scala",
            "ScalaRouting"),
          Link("1.3.Manipulating results", "/c1/1-3",
            "app/controllers/c1/S3ScalaResults.scala",
            "main/http/code/ScalaResults.scala",
            "ScalaResults"),
          Link("1.4.Session and Flash scopes", "/c1/1-4",
            "app/controllers/c1/S4ScalaSessionFlash.scala",
            "main/http/code/ScalaSessionFlash.scala",
            "ScalaSessionFlash"),
          Link("1.5.Body parsers", "/c1/1-5",
            "app/controllers/c1/S5ScalaBodyParsers.scala",
            "main/http/code/ScalaBodyParser.scala",
            "ScalaBodyParsers"),
          Link("1.6.Actions composition", "/c1/1-6",
            "app/controllers/c1/S6ScalaActionsComposition.scala",
            "main/http/code/ScalaActionsComposition.scala",
            "ScalaActionsComposition"),
          Link("1.7.Content negotiation", "/c1/1-7",
            "app/controllers/c1/S7ScalaContentNegotiation.scala",
            "main/http/code/ScalaContentNegotiation.scala",
            "ScalaContentNegotiation"),
          Link("1.8.Content negotiation", "/c1/1-8",
            "app/controllers/c1/S8ScalaErrorHandling.scala",
            "main/http/code/ScalaErrorHandling.scala",
            "ScalaErrorHandling")
        )),
        Chapter("2.Asynchronous HTTP programming", Seq(
          Link("2.1.Asynchronous results", "/c2/2-1",
            "app/controllers/c2/S1ScalaAsync.scala",
            "main/async/code/ScalaAsync.scala",
            "ScalaAsync"),
          Link("2.2.Streaming HTTP responses", "/c2/2-2",
            "app/controllers/c2/S2ScalaStream.scala",
            "",
            "ScalaStream"),
          Link("2.3.Comet sockets", "/c2/2-3",
            "app/controllers/c2/S3ScalaComet.scala",
            "main/async/code/ScalaComet.scala",
            "ScalaComet"),
          Link("2.4.WebSockets", "/c2/2-4",
            "app/controllers/c2/S4ScalaWebSockets.scala",
            "main/async/code/ScalaWebSockets.scala",
            "ScalaWebSockets")
        )),
        Chapter("3.The template engine (out of date)", Seq(
          Link("3.1.WebSockets", "/c3/3-1",
            "app/controllers/c3/S1ScalaTemplates.scala",
            "https://github.com/playframework/twirl/blob/1.0.x/docs/manual/working/scalaGuide/main/templates/code/ScalaTemplates.scala",
            "ScalaTemplates"),
          Link("3.2.Common use cases", "/c3/3-2",
            "app/controllers/c3/S2ScalaTemplateUseCases.scala",
            "https://github.com/playframework/twirl/tree/1.0.x/docs/manual/working/scalaGuide/main/templates/code/scalaguide/templates",
            "ScalaTemplateUseCases"),
          Link("3.3.Custom format", "/c3/3-3",
            "app/controllers/c3/S3ScalaCustomTemplateFormat.scala",
            "https://github.com/playframework/twirl/blob/1.0.x/compiler/src/test/scala/play/twirl/compiler/test/TemplateUtilsSpec.scala",
            "ScalaCustomTemplateFormat")
        )),
        Chapter("4.Form submission and validation", Seq(
          Link("4.1.Handling form submission", "/c4/4-1",
            "app/controllers/c4/S1ScalaForms.scala",
            "main/forms/code/ScalaForms.scala",
            "ScalaForms"),
          Link("4.2.Protecting against CSRF", "/c4/4-2",
            "app/controllers/c4/S2ScalaCsrf.scala",
            "main/forms/code/ScalaCsrf.scala",
            "ScalaCsrf"),
          Link("4.3.Custom Validations", "/c4/4-3",
            "app/controllers/c4/S3ScalaCustomValidations.scala",
            "main/forms/code/CustomValidations.scala",
            "ScalaCustomValidations"),
          Link("4.4.Custom Field Constructors", "/c4/4-4",
            "app/controllers/c4/S4ScalaCustomFieldConstructors.scala",
            "main/forms/code/ScalaFieldConstructor.scala",
            "ScalaCustomFieldConstructors")
        )),
        Chapter("5.Working with Json", Seq(
          Link("5.1.JSON basics", "/c5/5-1",
            "app/controllers/c5/S1ScalaJson.scala",
            "main/json/code/ScalaJsonSpec.scala",
            "ScalaJson"),
          Link("5.2.JSON with HTTP", "/c5/5-2",
            "app/controllers/c5/S2ScalaJsonHttp.scala",
            "main/json/code/ScalaJsonHttpSpec.scala",
            "ScalaJsonHttp"),
          Link("5.3.JSON Reads/Writes/Format Combinators", "/c5/5-3",
            "app/controllers/c5/S3ScalaJsonCombinators.scala",
            "main/json/code/ScalaJsonCombinatorsSpec.scala",
            "ScalaJsonCombinators"),
          Link("5.4.JSON Transformers", "/c5/5-4",
            "app/controllers/c5/S4ScalaJsonTransformers.scala",
            "",
            "ScalaJsonTransformers"),
          Link("5.5.JSON Macro Inception", "/c5/5-5",
            "app/controllers/c5/S5ScalaJsonInception.scala",
            "",
            "ScalaJsonInception")
        )),
        Chapter("10.Calling WebServices", Seq(
          Link("10.1.The Play WS API", "/c10/10-1",
            "app/controllers/c10/S1ScalaWS.scala",
            "main/ws/code/ScalaWSSpec.scala",
            "ScalaWS"),
          Link("10.2.Connecting to OpenID services", "/c10/10-2",
            "app/controllers/c10/S2ScalaOpenID.scala",
            "main/ws/code/ScalaOpenIdSpec.scala",
            "ScalaOpenID"),
          Link("10.3.Accessing resources protected by OAuth", "/c10/10-3",
            "app/controllers/c10/S3ScalaOAuth.scala",
            "",
            "ScalaOAuth")
        )),
        Chapter("11.Integrating with Akka", Seq(
          Link("11.Integrating with Akka", "/c11/11-1",
            "app/controllers/c11/S1ScalaAkka.scala",
            "main/akka/code/ScalaAkka.scala",
            "ScalaAkka")
        ))
      )
    )

    Ok(views.html.index(table))
  }
}

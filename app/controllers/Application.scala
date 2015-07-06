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
            "ScalaErrorHandling"),
          Link("2.1.Asynchronous results", "/c2/2-1",
            "app/controllers/c2/S1ScalaAsync.scala",
            "main/async/code/ScalaAsync.scala",
            "ScalaAsync"),
          Link("2.2.Streaming HTTP responses", "/c2/2-2",
            "app/controllers/c2/S2ScalaStream.scala",
            "main/async/code/",
            "ScalaStream"),
          Link("2.3.Comet sockets", "/c2/2-3",
            "app/controllers/c2/S3ScalaComet.scala",
            "main/async/code/ScalaComet.scala",
            "ScalaComet"),
          Link("2.4.WebSockets", "/c2/2-4",
            "app/controllers/c2/S4ScalaWebSockets.scala",
            "main/async/code/ScalaWebSockets.scala",
            "ScalaWebSockets")
        )
        ))
    )
    Ok(views.html.index(table))
  }
}

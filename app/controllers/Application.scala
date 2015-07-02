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
            "S4ScalaSessionFlash"),
          Link("1.5.Body parsers", "/c1/1-5",
            "app/controllers/c1/S5ScalaBodyParsers.scala",
            "main/http/code/ScalaBodyParsers.scala",
            "ScalaBodyParsers"),
          Link("1.6.Actions composition", "/c1/1-6",
            "app/controllers/c1/S6ScalaActionsComposition.scala",
            "main/http/code/ScalaActionsComposition.scala",
            "ScalaActionsComposition")
        )
        ))
    )
    Ok(views.html.index(table))
  }
}

package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {

    val links = Map(
      "1.1. Actions, Controllers and Results" -> Seq(
        "/c1/1-1-a1",
        "/c1/1-1-a2",
        "/c1/1-1-a3",
        "/c1/1-1-a4",
        "/c1/1-1-a5/anyword",
        "/c1/1-1-a6",
        "/c1/1-1-a7/ok",
        "/c1/1-1-a7/notFound",
        "/c1/1-1-a7/pageNotFound",
        "/c1/1-1-a7/oops",
        "/c1/1-1-a7/anyStatus",
        "/c1/1-1-a7/c1-1-1-a7",
        "/c1/1-1-a8",
        "/c1/1-1-a9/t/o/d/o"
      ),

      "1.2.HTTP Routing" -> Seq(
        "/c1/1-2-a1/1",
        "/c1/1-2-a2/999",
        "/c1/1-2-a3",
        "/c1/1-2-a3?page=2",
        "/c1/1-2-a4",
        "/c1/1-2-a4?a=1&version=1.0",
        "/c1/1-2-a5",
        "/c1/1-2-a6"
      ),

      "1.3.Manipulating results" -> Seq(
        "/c1/1-3-a1",
        "/c1/1-3-a2",
        "/c1/1-3-a3",
        "/c1/1-3-a4",
        "/c1/1-3-a5",
        "/c1/1-3-a6",
        "/c1/1-3-a7"
      ),

      "1.4.Session and Flash scopes" -> Seq(
        "/c1/1-4-a1",
        "/c1/1-4-a2",
        "/c1/1-4-a3",
        "/c1/1-4-a4",
        "/c1/1-4-a5",
        "/c1/1-4-a6",
        "/c1/1-4-a7",
        "/c1/1-4-a8"
      )

    )
    Ok(views.html.index(links))
  }

}

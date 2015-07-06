package controllers.c2

import play.api.libs.Comet
import play.api.libs.iteratee.{Enumeratee, Enumerator}
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by trydofor on 7/5/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaComet
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/async/code/ScalaComet.scala
 *
 */

class S3ScalaComet extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c2/2-3-a1",
      "/c2/2-3-a2",
      "/c2/2-3-a3",
      "/c2/2-3-a4"
    )

    Ok(views.html.list("2.3.Comet sockets", list))
  }


  def a1(flag: Boolean) = Action {
    if (flag) {
      val events = Enumerator(
        """<script>parent.showmsg('a1-1')</script>""",
        """<script>parent.showmsg('a1-2')</script>""",
        """<script>parent.showmsg('a1-3')</script>"""
      )
      Ok.chunked(events).as(HTML)
    } else {
      Ok(views.html.c2.s3("/c2/2-3-a1?f=true"))
    }
  }

  import play.twirl.api.Html

  val toCometMessage = Enumeratee.map[String] { data =>
    Html( """<script>parent.showmsg('""" + data + """')</script>""")
  }

  def a2(flag: Boolean) = Action {
    if (flag) {
      val events = Enumerator("a2-1", "a2-2", "a2-3")
      Ok.chunked(events &> toCometMessage)
    } else {
      Ok(views.html.c2.s3("/c2/2-3-a2?f=true"))
    }
  }

  def a3(flag: Boolean) = Action {
    if (flag) {
      val events = Enumerator("a3-1", "a3-2", "a3-3")
      Ok.chunked(events &> Comet(callback = "parent.showmsg"))
    } else {
      Ok(views.html.c2.s3("/c2/2-3-a3?f=true"))
    }
  }


  lazy val clock: Enumerator[String] = {

    import java.util._
    import java.text._
    import play.api.libs.concurrent.Promise
    import scala.concurrent.duration._

    val dateFormat = new SimpleDateFormat("HH:mm:ss")

    Enumerator.generateM {
      Promise.timeout(Some(dateFormat.format(new Date)), 1000 milliseconds)
    }
  }

  def a4(flag: Boolean) = Action {
    if (flag) Ok.chunked(clock &> Comet(callback = "parent.clock"))
    else Ok(views.html.c2.s3("/c2/2-3-a4?f=true"))
  }

}

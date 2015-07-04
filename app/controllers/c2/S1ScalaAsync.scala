package controllers.c2

import play.api.mvc._
import scala.concurrent.Future

/**
 * Created by trydofor on 7/2/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaAsync
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/async/code/ScalaAsync.scala
 *
 */

class S1ScalaAsync extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c2/2-1-a1?t=10",
      "/c2/2-1-a2?t=900",
      "/c2/2-1-a2?t=1500"
    )

    Ok(views.html.list("2.1.Asynchronous results", list))
  }

  import play.api.libs.concurrent.Execution.Implicits.defaultContext


  def intensiveComputation(t: Long) = {
    Thread.sleep(t)
    t
  }


  def a1(t: Long) = Action.async {
    val futureInt = Future {
      intensiveComputation(t)
    }
    futureInt.map(i => Ok("sleep(ms): " + i))
  }

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  import scala.concurrent.duration._

  def a2(t: Long) = Action.async {
    val futureInt = Future {
      intensiveComputation(t)
    }
    val timeoutFuture = play.api.libs.concurrent.Promise.timeout("timeout(>1 second)", 1.second)
    Future.firstCompletedOf(Seq(futureInt, timeoutFuture)).map {
      case i: Long => Ok("sleep(ms): " + i)
      case t: String => InternalServerError(t)
    }
  }
}

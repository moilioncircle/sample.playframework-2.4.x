package controllers.c2

import play.api.libs.iteratee.Enumerator
import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by trydofor on 7/5/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaStream
 *
 */

class S2ScalaStream extends Controller {

  val a0 = Action {
    val list = Seq(
      "/c2/2-2-a1",
      "/c2/2-2-a2",
      "/c2/2-2-a3",
      "/c2/2-2-a4",
      "/c2/2-2-a5",
      "/c2/2-2-a6",
      "/c2/2-2-a7",
      "/c2/2-2-a8"
    )

    Ok(views.html.list("2.2.Streaming HTTP responses", list))
  }


  def a1 = Action {
    Result(
      header = ResponseHeader(200),
      body = Enumerator("Hello World".getBytes("UTF8"))
    )
  }

  def a2 = Action {
    val file = new java.io.File("./README.md")
    val fileContent: Enumerator[Array[Byte]] = Enumerator.fromFile(file)

    Result(
      header = ResponseHeader(200),
      body = fileContent
    )
  }

  def a3 = Action {
    import play.api.libs.concurrent.Execution.Implicits._
    val file = new java.io.File("./README.md")
    val fileContent: Enumerator[Array[Byte]] = Enumerator.fromFile(file)

    Result(
      header = ResponseHeader(200, Map(CONTENT_LENGTH -> file.length.toString)),
      body = fileContent
    )
  }

  def a4 = Action {
    Ok.sendFile(new java.io.File("./README.md"))
  }

  def a5 = Action {
    Ok.sendFile(
      content = new java.io.File("./README.md"),
      fileName = _ => "termsOfService.pdf"
    )
  }

  def a6 = Action {
    Ok.sendFile(
      content = new java.io.File("./README.md"),
      inline = true
    )
  }

  def getDataStream = new java.io.FileInputStream("./README.md")
  def a7 = Action {
    val data = getDataStream
    val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(data)

    Ok.chunked(dataContent)
  }

  def a8 = Action {
    Ok.chunked(
      Enumerator("kiki", "foo", "bar").andThen(Enumerator.eof)
    )
  }
}

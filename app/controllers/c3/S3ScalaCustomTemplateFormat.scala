package controllers.c3

import play.api.http.{ContentTypes, ContentTypeOf}
import play.api.mvc._
import play.mvc.Http
import play.twirl.api.{Format, MimeTypes, BufferedContent}

import scala.collection.immutable

/**
 * Created by trydofor on 7/7/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaCustomTemplateFormat
 * @see /build.sbt TwirlKeys.templateFormats
 * @see /project/plugins.sbt sbt-twirl
 *
 */

/*
  1.addSbtPlugin in /project/plugins.sbt
  2.enablePlugins in  /build.sbt
  3.add TwirlKeys.templateFormats in /build.sbt
  4.add code as following
 */
object A9 {

  class Shi(elements: immutable.Seq[Shi], text: String) extends BufferedContent[Shi](elements, text) {
    def this(text: String) = this(Nil, text)

    def this(elements: immutable.Seq[Shi]) = this(elements, "")

    val contentType = MimeTypes.HTML
  }

  object ShiFormat extends Format[Shi] {
    def raw(text: String): Shi = new Shi(text.replaceAll("<br/>", "\n"))

    def escape(text: String): Shi = new Shi(text.replaceAll("\n", "<br/>"))

    val empty = new Shi(Nil)

    def fill(elements: immutable.Seq[Shi]) = new Shi(elements)
  }

  implicit def contentTypeHttp(implicit codec: Codec): ContentTypeOf[Shi] =
    ContentTypeOf[Shi](Some(ContentTypes.HTML))
}


class S3ScalaCustomTemplateFormat extends Controller {

  val a0 = Action {
    Ok(views.shi.c3.s3(
      """
      1 - addSbtPlugin in /project/plugins.sbt
      2 - enablePlugins in  /build.sbt
      3 - add TwirlKeys.templateFormats in /build.sbt
      4 - add code as following
      """))
  }
}

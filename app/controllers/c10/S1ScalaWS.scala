package controllers.c10

import javax.inject.Inject
import scala.concurrent.Future

import play.api.mvc._
import play.api.libs.ws._
/**
 * Created by trydofor on 7/10/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaWS
 * @see https://github.com/playframework/playframework/blob/2.4.x/documentation/manual/working/scalaGuide/main/ws/code/ScalaWSSpec.scala
 * @see /build.sbt libraryDependencies  ws
 */

case class Person(name: String, age: Int)

class S1ScalaWS @Inject() (ws: WSClient) extends Controller {

  val a0 = Action {
    val url = "http://127.0.0.1:9000/c10/10-1-a1"
    val request: WSRequest = ws.url(url)

    val complexRequest: WSRequest =
      request.withHeaders("Accept" -> "text/html")
        .withRequestTimeout(10000)
        .withQueryString("search" -> "play")

    val futureResponse: Future[WSResponse] = complexRequest.get()

    ws.url(url).withAuth("user", "password", WSAuthScheme.BASIC).get()
    ws.url(url).withFollowRedirects(true).get()
    ws.url(url).withQueryString("paramKey" -> "paramValue").get()
    ws.url(url).withHeaders("Content-Type" -> "application/xml").post(<b></b>)
    ws.url(url).withVirtualHost("192.168.1.1").get()
    ws.url(url).withRequestTimeout(5000).get()
    ws.url(url).post(Map("key" -> Seq("value")))


    object json{
      import play.api.libs.json._
      val data = Json.obj(
        "key1" -> "value1",
        "key2" -> "value2"
      )
      val futureResponse: Future[WSResponse] = ws.url(url).post(data)
    }

    object xml{
      val data = <person>
        <name>Steve</name>
        <age>23</age>
      </person>
      val futureResponse: Future[WSResponse] = ws.url(url).post(data)
    }


    implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

    val v1: Future[String] = ws.url(url).get().map {
      response =>
        (response.json \ "person" \ "name").as[String]
    }

    import play.api.libs.json._

    implicit val personReads = Json.reads[Person]

    val v2: Future[JsResult[Person]] = ws.url(url).get().map {
      response => (response.json \ "person").validate[Person]
    }

    val v3: Future[scala.xml.NodeSeq] = ws.url(url).get().map {
      response =>
        response.xml \ "message"
    }


    object large {
      import play.api.libs.iteratee._

      // Make the request
      val futureResponse: Future[(WSResponseHeaders, Enumerator[Array[Byte]])] =
        ws.url(url).getStream()

      val bytesReturned: Future[Long] = futureResponse.flatMap {
        case (headers, body) =>
          // Count the number of bytes returned
          body |>>> Iteratee.fold(0l) { (total, bytes) =>
            total + bytes.length
          }
      }
    }

    object download{
      import play.api.libs.iteratee._
      import java.io._

      val file = File.createTempFile("stream-to-file-", ".txt")
      // Make the request
      val futureResponse: Future[(WSResponseHeaders, Enumerator[Array[Byte]])] =
        ws.url(url).getStream()

      val downloadedFile: Future[File] = futureResponse.flatMap {
        case (headers, body) =>
          val outputStream = new FileOutputStream(file)

          // The iteratee that writes to the output stream
          val iteratee = Iteratee.foreach[Array[Byte]] { bytes =>
            outputStream.write(bytes)
          }

          // Feed the body into the iteratee
          (body |>>> iteratee).andThen {
            case result =>
              // Close the output stream whether there was an error or not
              outputStream.close()
              // Get the result or rethrow the error
              result.get
          }.map(_ => file)
      }


      def downloadFile = Action.async {

        // Make the request
        ws.url(url).getStream().map {
          case (response, body) =>

            // Check that the response was successful
            if (response.status == 200) {

              // Get the content type
              val contentType = response.headers.get("Content-Type").flatMap(_.headOption)
                .getOrElse("application/octet-stream")

              // If there's a content length, send that, otherwise return the body chunked
              response.headers.get("Content-Length") match {
                case Some(Seq(length)) =>
                  Ok.feed(body).as(contentType).withHeaders("Content-Length" -> length)
                case _ =>
                  Ok.chunked(body).as(contentType)
              }
            } else {
              BadGateway
            }
        }
      }
    }

    object chaining {
      val urlOne = url
      val exceptionUrl = url
      val futureResponse: Future[WSResponse] = for {
        responseOne <- ws.url(urlOne).get()
        responseTwo <- ws.url(responseOne.body).get()
        responseThree <- ws.url(responseTwo.body).get()
      } yield responseThree

      futureResponse.recover {
        case e: Exception =>
          val exceptionData = Map("error" -> Seq(e.getMessage))
          ws.url(exceptionUrl).post(exceptionData)
      }

      def wsAction = Action.async {
        ws.url(url).get().map { response =>
          Ok(response.body)
        }
      }
    }

    object ning{
      import play.api.libs.ws.ning._

      implicit val sslClient = NingWSClient()
      // close with sslClient.close() when finished with client
      val response1 = WS.clientUrl(url).get()
      val response2 = sslClient.url(url).get()


      object PairMagnet {
        implicit def fromPair(pair: (WSClient, java.net.URL)) =
          new WSRequestMagnet {
            def apply(): WSRequest = {
              val (client, netUrl) = pair
              client.url(netUrl.toString)
            }
          }
      }

      import scala.language.implicitConversions
      import PairMagnet._

      val exampleURL = new java.net.URL(url)
      val response = WS.url(ws -> exampleURL).get()

      import com.typesafe.config.ConfigFactory
      import play.api._
      import play.api.libs.ws._
      import play.api.libs.ws.ning._
      import java.io._

      val configuration = Configuration.reference ++ Configuration(ConfigFactory.parseString(
        """
          |ws.followRedirects = true
        """.stripMargin))

      // If running in Play, environment should be injected
      val environment = Environment(new File("."), this.getClass.getClassLoader, Mode.Prod)

      val parser = new WSConfigParser(configuration, environment)
      val config = new NingWSClientConfig(wsClientConfig = parser.parse())
      val builder = new NingAsyncHttpClientConfigBuilder(config)

      import com.ning.http.client.AsyncHttpClient

      val client: AsyncHttpClient = ws.underlying
    }

    Ok(
      """
      10.1.The Play WS API

      https://github.com/AsyncHttpClient/async-http-client/

      WS does not support multi part form upload directly. You can use the underlying client with RequestBuilder.addBodyPart.

      WS does not support streaming body upload. In this case, you should use the FeedableBodyGenerator provided by AsyncHttpClient.

      |play.ws.ning.allowPoolingConnection
      |play.ws.ning.allowSslConnectionPool
      |play.ws.ning.ioThreadMultiplier
      |play.ws.ning.maxConnectionsPerHost
      |play.ws.ning.maxConnectionsTotal
      |play.ws.ning.maxConnectionLifeTime
      |play.ws.ning.idleConnectionInPoolTimeout
      |play.ws.ning.webSocketIdleTimeout
      |play.ws.ning.maxNumberOfRedirects
      |play.ws.ning.maxRequestRetry
      |play.ws.ning.disableUrlEncoding
      """)
  }


  val a1 = Action{
    Ok("ok")
  }
}

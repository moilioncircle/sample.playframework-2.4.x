package controllers.c3

import controllers.Assets
import play.api.Logger
import play.api.mvc._

/**
 * Created by trydofor on 7/7/15.
 * @see https://playframework.com/documentation/2.4.x/ScalaTemplates
 */

case class Customer(firstName:String,lastName:String){
  val name = firstName +" . "+ lastName
}

case class Order(title: String,price:Double)

class S1ScalaTemplates extends Controller {

  val a0 = Action {
    val c = Customer("shi","rongjiu")
    val o = List(Order("moilioncircle",99.99))
    implicit val html = "<b>escaped html</b>"
    val content = views.html.c3.s1(c, o)
    Ok(content)
  }
}

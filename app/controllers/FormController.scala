package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents, Request}

import javax.inject._

@Singleton
class FormController  @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) with play.api.i18n.I18nSupport{

  def simpleForm() = Action {  implicit request: Request[AnyContent] =>
    Ok(views.html.form(BasicForm.form))
  }

  def simpleFormPost() = Action { implicit request =>
    val postData = request.body.asFormUrlEncoded
    postData.map{args =>
      val vehicleName = args("Vehicle Name").head
      println(vehicleName.getClass.getSimpleName)
      Ok(s" You've introduced the ${vehicleName}")
    }.getOrElse(Ok("Error"))

  }


}

case class BasicForm(name: String)

object BasicForm {
  val form: Form[BasicForm] = Form(
    mapping(
      "name" -> text,
    )(BasicForm.apply)(BasicForm.unapply)
  )
}


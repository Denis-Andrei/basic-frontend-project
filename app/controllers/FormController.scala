package controllers

import models.Vehicle
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws
import play.api.libs.ws._
import play.api.libs.ws.WSResponse
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import javax.inject._
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class FormController  @Inject()(ws: WSClient,cc: ControllerComponents, implicit val ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport{

  def simpleForm() = Action {  implicit request: Request[AnyContent] =>
    Ok(views.html.form(BasicForm.form))
  }

  def simpleFormPost() = Action.async { implicit request =>
    val postData = request.body.asFormUrlEncoded

    val vehicleName = postData.map{args =>
      args("Vehicle Name").head
    }.getOrElse(Ok("Error"))

    val dataToBeSend = Json.obj(
      "Vehicle Name" -> s"$vehicleName"
    )

    val futureResponse: Future[WSResponse] = ws.url("http://localhost:9001/form").post(dataToBeSend)

    futureResponse.map {
      response =>
        val js = Json.fromJson[Vehicle](response.json)
        println("js: " + js)
        val veh = js.get
        println("veh: " + veh)
        Ok(views.html.vehicle(veh))
    }recover {

      case e =>
        println("e: " + e.printStackTrace())
        NotFound
    }

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


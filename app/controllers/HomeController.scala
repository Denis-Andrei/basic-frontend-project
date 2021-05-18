package controllers

import models.Vehicle

import javax.inject._
import play.api._
import play.api.libs.json.{JsResult, Json}
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.runtime.Nothing$
import scala.util.{Failure, Success}


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(ws: WSClient, val controllerComponents: ControllerComponents, ec: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

    def index() = Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())

    }


    def vehicle(vehicleName: String) = Action.async { implicit request: Request[AnyContent] =>

      val futureResult = ws.url(s"http://localhost:9001/checkVehicle/${vehicleName}").get()

      futureResult.map { response =>
              val js = Json.fromJson[Vehicle](response.json)
              val veh = js.get
              Ok(views.html.vehicle(veh))
      } recover {
        case _ => NotFound
      }
    }

}







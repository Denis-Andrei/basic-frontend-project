package controllers

import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.http.Status.{NOT_FOUND, OK}
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{BodyWritable, WSClient, WSRequest, WSResponse}
import play.api.test.Helpers.{GET, POST, contentAsString, contentType, defaultAwaitTimeout, status, stubControllerComponents}
import play.api.test.{FakeRequest, Injecting}

import scala.concurrent.{ExecutionContext, Future}

class FormControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  lazy val wsMock: WSClient = mock[WSClient]
  lazy val wsRequest: WSRequest = mock[WSRequest]
  lazy val wsResponse: WSResponse = mock[WSResponse]



  "FormController GET" should {

    "render the simpleForm page from a new instance of controller" in {
      val controller = new FormController(ws, stubControllerComponents(), executionContext)
      val simpleForm = controller.simpleForm().apply(FakeRequest(GET, "/"))

      status(simpleForm) mustBe OK
      contentType(simpleForm) mustBe Some("text/html")
      contentAsString(simpleForm) must include("forms")
    }

    "render the simpleForm page from the application" in {
      val controller = inject[FormController]
      val home = controller.simpleForm().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("forms")
    }


  "FormController POST " should {

    "render the simpleFormPost page from a new instance of controller" in {

      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val result = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))

      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 200
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)
      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.successful(wsResponse)


      status(result) mustBe OK
      contentType(result) mustBe Some("text/html")
      contentAsString(result) must include("Vehicle")
    }

    "fail to render the simpleFormPost page from a new instance of controller" in {

      lazy val controller = new FormController(wsMock, stubControllerComponents(), executionContext)
      lazy val result = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))

      when(wsMock.url(ArgumentMatchers.any())) thenReturn wsRequest
      when(wsResponse.status) thenReturn 404
      when(wsResponse.json) thenReturn Json.parse(
        """{
          | "wheels": 4,
          | "heavy": true,
          | "name": "BMW"
          |}""".stripMargin)

      when(wsRequest.post(any[JsObject]())(any[BodyWritable[JsObject]]())) thenReturn Future.failed(new Exception)


      status(result) mustBe NOT_FOUND

    }

  }



  }
}
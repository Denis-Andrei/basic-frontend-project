package controllers

import org.scalatestplus.easymock.EasyMockSugar.mock
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.http.Status.OK
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.Helpers.{GET, POST, contentAsString, contentType, defaultAwaitTimeout, status, stubControllerComponents}
import play.api.test.{FakeRequest, Injecting}

import scala.Option.when
import scala.concurrent.{ExecutionContext, Future}

class FormControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]
  lazy val wsMock = mock[WSClient]
  lazy val wsMockResponse = mock[WSResponse]


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
      val controller = new FormController(ws, stubControllerComponents(), executionContext)
      val simpleForm = controller.simpleFormPost().apply(FakeRequest(POST, "/simpleForm"))
      when(wsMockResponse.post(FakeRequest(POST, "/simpleForm")))
        .thenReturn(Future[WSResponse])

      status(simpleForm) mustBe OK
      contentType(simpleForm) mustBe Some("text/html")
      contentAsString(simpleForm) must include("forms")
    }

  }



  }
}
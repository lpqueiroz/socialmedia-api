package controllers

import controllers.users.UserController
import fixtures.{CommentFixtures, UserFixtures}
import models.User.{EmailAddressAlreadyExistsException, InvalidEmailAddressException, InvalidUserNameCharactersException, User, UserNotFoundException}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsJson, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

class UserControllerSpec(implicit ex: ExecutionEnv) extends Specification{

  case class TestData(users: ArrayBuffer[User] = ArrayBuffer.empty,
                      successOrFailure: Future[User] = Future.failed(new Exception()))

  def userController(_testData: TestData) = new UserController(
    UserFixtures.userStubService(_testData.successOrFailure), stubControllerComponents()
  )

  "This is the specification of UserController" >> {
    "insert" >> {
      "if the form is invalid" >> {
        "must return 400 BAD REQUEST" >> {
          val user = User(1, "randomname", "random32131")
          val controller: UserController = userController(TestData(ArrayBuffer(user), Future.successful(user)))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("nameeee" -> 43242, "email" -> 321321)).withJsonBody(Json.parse(
            """      {
               "nameeee":43242,
               "email":321321
            }"""
          ))

          val result: Future[Result] = controller.register()(request)

          status(result) must beEqualTo(400)
        }
      }
      "if no exceptions return from service and form is valid" >> {
        "must return 200 OK" >> {
          val user = User(1, "randomname", "random32131")
          val controller: UserController = userController(TestData(ArrayBuffer(user), Future.successful(user)))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("name" -> "larissa", "email" -> "random@gmail.com")).withJsonBody(Json.parse(
            """      {
               "name":"larissa",
               "email":"random@gmail.com"
            }"""
          ))

          val result: Future[Result] = controller.register()(request)

          status(result) must beEqualTo(200)
        }
      }
      "if the exception InvalidUserNameCharactersException return from service" >> {
        "must return 400 BAD REQUEST" >> {
          val controller: UserController = userController(TestData(successOrFailure = Future.failed(InvalidUserNameCharactersException("231312"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("name" -> "larissa", "email" -> "random@gmail.com")).withJsonBody(Json.parse(
            """      {
               "name":43242,
               "email":"random@gmail.com"
            }"""
          ))
          val result: Future[Result] = controller.register()(request)

          status(result) must beEqualTo(400)
        }
      }
      "if the exception InvalidEmailAddressException return from service" >> {
        "must return 400 BAD REQUEST" >> {
          val controller: UserController = userController(TestData(successOrFailure = Future.failed(InvalidEmailAddressException("231312"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("name" -> "larissa", "email" -> "random@gmail.com")).withJsonBody(Json.parse(
            """      {
               "name":43242,
               "email":"random@gmail.com"
            }"""
          ))
          val result: Future[Result] = controller.register()(request)

          status(result) must beEqualTo(400)
        }
      }
      "if the exception EmailAddressAlreadyExistsException  return from service" >> {
        "must return 409 CONFLICT" >> {
          val controller: UserController = userController(TestData(successOrFailure = Future.failed(EmailAddressAlreadyExistsException("larissaqueiroz.p@gmail.com"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("name" -> "larissa", "email" -> "random@gmail.com")).withJsonBody(Json.parse(
            """      {
               "name":43242,
               "email":"random@gmail.com"
            }"""
          ))
          val result: Future[Result] = controller.register()(request)

          status(result) must beEqualTo(409)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: UserController = userController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("name" -> "larissa", "email" -> "random@gmail.com")).withJsonBody(Json.parse(
            """      {
               "name":43242,
               "email":"random@gmail.com"
            }"""
          ))
          val result: Future[Result] = controller.register()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
  }
}

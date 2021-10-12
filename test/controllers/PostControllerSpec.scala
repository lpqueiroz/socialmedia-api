package controllers

import java.sql.Timestamp
import java.time.LocalDateTime
import akka.stream.Materializer
import auth.{AuthAction, AuthService}
import controllers.posts.PostController
import fixtures.{ConfigurationFixtures, MockJwtRequest, PostFixtures}
import models.Post.{Post, PostNotFoundException}
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsJson, BodyParsers, MultipartFormData, Result}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

class PostControllerSpec(implicit ev: ExecutionEnv) extends Specification {

  val application = new GuiceApplicationBuilder().build()
  implicit val mat: Materializer = application.materializer

  case class TestData(posts: ArrayBuffer[Post] = ArrayBuffer.empty,
                      successOrFailure: Future[Seq[Post]] = Future.failed(new Exception()))

  def postController(_testData: TestData) = new PostController(
    PostFixtures.postStubService(_testData.posts, _testData.successOrFailure),
    stubControllerComponents(),
    new AuthAction(new BodyParsers.Default(), new AuthService(ConfigurationFixtures.emptyConf))
  )

  val token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkYyZGdFMzBRV09fdkUxNDF4cXctdyJ9.eyJpc3MiOiJodHRwczovL3NvY2lhbG1lZGlhLWFwaS51cy5hdXRoMC5jb20vIiwic3ViIjoiY0g1T1AxOVNLaDcxTzk4WDU4TElENXpKemJtWFZsRFlAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vc29jaWFsbWVkaWEtYXBpLnVzLmF1dGgwLmNvbS9hcGkvdjIvIiwiaWF0IjoxNjM0MDAwNDk5LCJleHAiOjE2MzQwODY4OTksImF6cCI6ImNINU9QMTlTS2g3MU85OFg1OExJRDV6SnpibVhWbERZIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.c2SG2OeP9pBQ1Jirvqp3fJccsyZsP5k92GvGVNvS3YkpiFKZEyUKxkV9DYKWS6wbNDSxIwWIJ69iGSf_b-rml6ciEAknVmws-hItKHpTtpO67SXoJshxhqKb3YimaV5pKjSkJO86NzAjAuqn36KTMig8fEsvgJsrqf-LopZE3RmnAQovF-bWPp41NGSnuk_MUJNtUnbTejpUIkZiq8bA9o68wjYVf89102ONms21SBZJhQQKC5kP-fPYb4bGx51ZHoAUa1qq8B2bXpyPOMBEklEw_AgLKObYZsR2e96wfL9eCfpeib8r8n9Bn9N6t33QfLjRb9Tq3X1uevvSJG6s-w"

  "This is the specification of postController" >> {
    "insert" >> {
      "if the form is invalid" >> {
        "must return 400 BAD REQUEST" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now()), 1)
          val controller: PostController = postController(TestData(ArrayBuffer(post), Future.successful(ArrayBuffer(post))))

          val request = MockJwtRequest.withToken(Some(token)).withMultipartFormDataBody(
            MultipartFormData(dataParts = Map("text" -> Seq("lalala")), Seq.empty, Seq.empty)
          )

          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(400)
        }
      }
      "if the form is valid" >> {
        "must return 200 OK" >> {
         pending
        }
      }
      "if the exception UserNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
         pending
        }
      }
      "if the exception PostNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
         pending
        }
      }
      "if the exception PostNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
         pending
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
         pending
        }
      }
    }
    "list" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
         pending
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          pending
        }
      }
    }
    "listByUser" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now()), 1)
          val controller = postController(TestData(ArrayBuffer(post), Future.successful(ArrayBuffer(post))))

          val request = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "createdBy" -> 1)).withMultipartFormDataBody(
            MultipartFormData(dataParts = Map("text" -> Seq("lalala")), Seq.empty, Seq.empty)
          )

          val result: Future[Result] = controller.listByUser(1)(request)

          status(result) must beEqualTo(200)
        }
      }
      "if the exception UserNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          pending
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          pending
        }
      }
    }
    "listByAscendingOrder" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now()), 1)
          val controller = postController(TestData(ArrayBuffer(post), Future.successful(ArrayBuffer(post))))

          val request = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "createdBy" -> 1)).withMultipartFormDataBody(
            MultipartFormData(dataParts = Map("text" -> Seq("lalala")), Seq.empty, Seq.empty)
          )

          val result: Future[Result] = controller.listByAscendingOrder()(request)

          status(result) must beEqualTo(200)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller = postController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "createdBy" -> 1)).withMultipartFormDataBody(
            MultipartFormData(dataParts = Map("text" -> Seq("lalala")), Seq.empty, Seq.empty)
          )
          val result: Future[Result] = controller.listByAscendingOrder()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "listByDescendingOrder" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now()), 1)
          val controller = postController(TestData(ArrayBuffer(post), Future.successful(ArrayBuffer(post))))

          val request = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "createdBy" -> 1)).withMultipartFormDataBody(
            MultipartFormData(dataParts = Map("text" -> Seq("lalala")), Seq.empty, Seq.empty)
          )

          val result: Future[Result] = controller.listByDescendingOrder()(request)

          status(result) must beEqualTo(200)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller = postController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "createdBy" -> 1)).withMultipartFormDataBody(
            MultipartFormData(dataParts = Map("text" -> Seq("lalala")), Seq.empty, Seq.empty)
          )
          val result: Future[Result] = controller.listByDescendingOrder()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "update" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
         pending
        }
      }
      "if the form is invalid" >> {
        "must return 400 BAD REQUEST" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now()), 1)
          val controller = postController(TestData(ArrayBuffer(post), Future.successful(ArrayBuffer(post))))

          val request = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "createdBy" -> 1)).withMultipartFormDataBody(
            MultipartFormData(dataParts = Map("text" -> Seq("lalala")), Seq.empty, Seq.empty)
          )

          val result: Future[Result] = controller.update(1)(request)

          status(result) must beEqualTo(400)
        }
      }
      "if the exception PostNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          pending
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
         pending
        }
      }
    }
  }

}

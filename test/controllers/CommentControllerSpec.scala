package controllers

import java.sql.Timestamp
import java.time.LocalDateTime
import controllers.comments.CommentController
import fixtures.CommentFixtures
import models.Comment.{Comment, CommentNotFoundException}
import models.Post.PostNotFoundException
import models.User.UserNotFoundException
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsJson, Result}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class CommentControllerSpec(implicit ev: ExecutionEnv) extends Specification {

  case class TestData(comments: ArrayBuffer[Comment] = ArrayBuffer.empty,
                      successOrFailure: Future[Seq[Comment]] = Future.failed(new Exception()))

  def commentController(_testData: TestData) = new CommentController(
    CommentFixtures.commentStubService(_testData.comments, _testData.successOrFailure), stubControllerComponents()
  )

  "This is the specification of CommentController" >> {
    "insert" >> {
      "if the form is invalid" >> {
        "must return 400 BAD REQUEST" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"ra3213",
               "postId":"lalalal",
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(400)
        }
      }
      "if the form is valid" >> {
        "must return 200 OK" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(200)
        }
      }
      "if the exception UserNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(UserNotFoundException())))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(404)
        }
      }
      "if the exception CommentNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(CommentNotFoundException())))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(404)
        }
      }
      "if the exception PostNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(PostNotFoundException())))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(404)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "list" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.list()(request)

          status(result) must beEqualTo(200)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.list()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "listByUser" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.listByUser(1)(request)

          status(result) must beEqualTo(200)
        }
      }
      "if the exception UserNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(UserNotFoundException())))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.insert()(request)

          status(result) must beEqualTo(404)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.list()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "listByAscendingOrder" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.listByAscendingOrder()(request)

          status(result) must beEqualTo(200)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.listByAscendingOrder()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "listByDescendingOrder" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.listByDescendingOrder()(request)

          status(result) must beEqualTo(200)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.listByDescendingOrder()(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "update" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.update(1)(request)

          status(result) must beEqualTo(200)
        }
      }
      "if the form is invalid" >> {
        "must return 400 BAD REQUEST" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"ra3213",
               "postId":"lalalal",
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.update(1)(request)

          status(result) must beEqualTo(400)
        }
      }
      "if the exception CommentNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(CommentNotFoundException())))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.update(1)(request)

          status(result) must beEqualTo(404)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.update(1)(request)

          status(result) must beEqualTo(500)
        }
      }
    }
    "listByPost" >> {
      "if no exception returns from service" >> {
        "must return 200 OK" >> {
          val comment = Comment(1, "randomid", Timestamp.valueOf(LocalDateTime.now()), 1, 1)
          val controller: CommentController = commentController(TestData(ArrayBuffer(comment), Future.successful(ArrayBuffer(comment))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))

          val result: Future[Result] = controller.listByPost(1)(request)

          status(result) must beEqualTo(200)
        }
      }
      "if the exception PostNotFoundException returns from service" >> {
        "must return 404 NOT FOUND" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(PostNotFoundException())))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.listByPost(1)(request)

          status(result) must beEqualTo(404)
        }
      }
      "if an unknown exception returns from service" >> {
        "must return 500 INTERNAL SERVER ERROR" >> {
          val controller: CommentController = commentController(TestData(successOrFailure = Future.failed(new Exception("random exception"))))

          val request: FakeRequest[AnyContentAsJson] = FakeRequest("POST", "fakeuri", FakeHeaders(),
            Json.obj("text" -> "randomtext", "postId" -> 1, "createdBy" -> 1)).withJsonBody(Json.parse(
            """      {
               "text":"randm",
               "postId":1,
               "createdBy":1
            }"""
          ))
          val result: Future[Result] = controller.listByPost(1)(request)

          status(result) must beEqualTo(500)
        }
      }
    }
  }
}

package services

import java.io.File
import java.net.URI
import java.sql.Timestamp
import java.time.{Clock, LocalDateTime}

import fixtures.{ClockFixture, CommentFixtures}
import models.Comment.{Comment, CommentNotFoundException}
import models.Post.{Post, PostNotFoundException}
import models.User.{User, UserNotFoundException}
import org.specs2.mutable.Specification
import org.specs2.concurrent.ExecutionEnv
import repositories.comments.CommentRepository
import repositories.posts.PostRepository
import repositories.users.UserRepository
import services.comments.CommentServiceImpl

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

class CommentServiceSpec(implicit ev: ExecutionEnv) extends Specification {

  object TestFixture extends ClockFixture

  import TestFixture._

  case class TestData(s: ArrayBuffer[Comment] = ArrayBuffer.empty)

  private def serviceWithRepoReturningException() = new CommentServiceImpl(
    new CommentRepository {
      override def list()(implicit ex: ExecutionContext): Future[Seq[Comment]] = Future.failed(new Exception())

      override def insert(Comment: Comment)(implicit ex: ExecutionContext): Future[Comment] = Future.failed(new Exception())

      override def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Comment]] = Future.failed(new Exception())

      override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = Future.failed(new Exception())

      override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = Future.failed(new Exception())

      override def update(Comment: Comment)(implicit ex: ExecutionContext, clock: Clock): Future[Comment] = Future.failed(new Exception())

      override def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Comment]] = Future.failed(new Exception())

      override def listByPost(post: Post)(implicit ex: ExecutionContext): Future[Seq[Comment]] = Future.failed(new Exception())
    }, new UserRepository {
      override def register(user: User)(implicit ex: ExecutionContext): Future[User] = Future.failed(new Exception())

      override def findById(userId: Long)(implicit ex: ExecutionContext): Future[Option[User]] = Future.failed(new Exception())

      override def list()(implicit ex: ExecutionContext): Future[Seq[User]] = Future.failed(new Exception())
    }, new PostRepository {
      override def list()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def insert(post: Post)(implicit ex: ExecutionContext): Future[Post] = Future.failed(new Exception())

      override def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def update(post: Post)(implicit ex: ExecutionContext, clock: Clock): Future[Post] = Future.failed(new Exception())

      override def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Post]] = Future.failed(new Exception())
    }
  )

  "This is the specification of CommentService" >> {
    "insert" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .insert("random Comment", 1, 1)

          result must throwA[Exception].await
        }
        "must return an instance of Comment" >> {
          val comment = Comment(1, "randomtext", Timestamp.valueOf(LocalDateTime.now(defaultClock)), 1, 1)
          val service = CommentFixtures.commentTestService()

          val result: Future[Comment] = service.insert(comment.text, comment.createdBy, 1)

          result must beAnInstanceOf[Comment].await
        }
      }
    }
    "listByUser" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .listByUser(1)

          result must throwA[Exception].await
        }
        "must return an instance of Seq[Comment]" >> {
          val user = User(1, "Larissa", "larissaqueiroz.p@gmail.com")
          val service = CommentFixtures.commentTestService(users = ArrayBuffer(user))

          val result: Future[Seq[Comment]] = service.listByUser(1)

          result must beAnInstanceOf[Seq[Comment]].await
        }
        "must return UserNotFoundException" >> {
          val service = CommentFixtures.commentTestService()

          val result: Future[Seq[Comment]] = service.listByUser(1)

          result must throwA[UserNotFoundException].await
        }
      }
    }
    "list" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .listByUser(1)

          result must throwA[Exception].await
        }
        "must return an instance of Seq[Comment]" >> {
          val service = CommentFixtures.commentTestService()

          val result: Future[Seq[Comment]] = service.list()

          result must beAnInstanceOf[Seq[Comment]].await
        }
      }
    }
    "listByAscendingOrder" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .listByUser(1)

          result must throwA[Exception].await
        }
        "must return an instance of Seq[Comment]" >> {
          val service = CommentFixtures.commentTestService()

          val result: Future[Seq[Comment]] = service.listByAscendingOrder()

          result must beAnInstanceOf[Seq[Comment]].await
        }
      }
    }
    "listByDescendingOrder" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .listByUser(1)

          result must throwA[Exception].await
        }
        "must return an instance of Seq[Comment]" >> {
          val service = CommentFixtures.commentTestService()

          val result: Future[Seq[Comment]] = service.listByDescendingOrder()

          result must beAnInstanceOf[Seq[Comment]].await
        }
      }
    }
    "update" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .listByUser(1)

          result must throwA[Exception].await
        }
        "must return an instance of Comment" >> {
          val comment = Comment(1, "randomtext", Timestamp.valueOf(LocalDateTime.now(defaultClock)), 1, 1)
          val service = CommentFixtures.commentTestService(comments = ArrayBuffer(comment))

          val result: Future[Comment] = service.update(1, "random-text")

          result must beAnInstanceOf[Comment].await
        }
        "must return CommentNotFoundExeption" >> {
          val service = CommentFixtures.commentTestService()

          val result: Future[Comment] = service.update(1, "random-text")

          result must throwA[CommentNotFoundException].await
        }
      }
    }
    "listByPost" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .listByPost(1)

          result must throwA[Exception].await
        }
        "must return an instance of Seq[Comment]" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now(defaultClock)), 1)
          val comment = Comment(1, "randomtext", Timestamp.valueOf(LocalDateTime.now(defaultClock)), 1, 1)
          val service = CommentFixtures.commentTestService(comments = ArrayBuffer(comment), posts = ArrayBuffer(post))

          val result: Future[Seq[Comment]] = service.listByPost(1)

          result must beAnInstanceOf[Seq[Comment]].await
        }
        "must return PostNotFoundException" >> {
          val service = CommentFixtures.commentTestService()

          val result: Future[Seq[Comment]] = service.listByPost(1)

          result must throwA[PostNotFoundException].await
        }
      }
    }
  }

}

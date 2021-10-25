package services

import java.io.File
import java.net.URI
import java.sql.Timestamp
import java.time.{Clock, LocalDateTime}

import fixtures.{ClockFixture, PostFixtures}
import models.Post.{Post, PostNotFoundException}
import models.User.{User, UserNotFoundException}
import org.specs2.mutable.Specification
import org.specs2.concurrent.ExecutionEnv
import repositories.files.FileRepository
import repositories.posts.PostRepository
import repositories.users.UserRepository
import services.posts.PostServiceImpl

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

class PostServiceSpec(implicit ev: ExecutionEnv) extends Specification {

  object TestFixture extends ClockFixture

  import TestFixture._

  case class TestData(posts: ArrayBuffer[Post] = ArrayBuffer.empty)

  private def serviceWithRepoReturningException() = new PostServiceImpl(
    new PostRepository {
      override def list()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def insert(post: Post)(implicit ex: ExecutionContext): Future[Post] = Future.failed(new Exception())

      override def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.failed(new Exception())

      override def update(post: Post)(implicit ex: ExecutionContext, clock: Clock): Future[Post] = Future.failed(new Exception())

      override def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Post]] = Future.failed(new Exception())
    }, new UserRepository {
      override def register(user: User)(implicit ex: ExecutionContext): Future[User] = Future.failed(new Exception())

      override def findById(userId: Long)(implicit ex: ExecutionContext): Future[Option[User]] = Future.failed(new Exception())

      override def list()(implicit ex: ExecutionContext): Future[Seq[User]] = Future.failed(new Exception())

      override def findByEmail(email: String)(implicit ex: ExecutionContext): Future[Option[User]] = Future.failed(new Exception())
    }, new FileRepository {
      override def saveFile(imagePath: String, imageFile: File): URI = URI.create("")
    }
  )

  "This is the specification of PostService" >> {
    "insert" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .insert("random post", new File("/randompath"), "/random/path", 1)

          result must throwA[Exception].await
        }
        "must return an instance of Post" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now(defaultClock)), 1)
          val service = PostFixtures.postTestService()

          val result: Future[Post] = service.insert(post.text, new File("/randompath"), post.imagePath, post.createdBy)

          result must beAnInstanceOf[Post].await
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
        "must return an instance of Seq[Post]" >> {
          val user = User(1, "Larissa", "larissaqueiroz.p@gmail.com")
          val service = PostFixtures.postTestService(users = ArrayBuffer(user))

          val result: Future[Seq[Post]] = service.listByUser(1)

          result must beAnInstanceOf[Seq[Post]].await
        }
        "must return UserNotFoundException" >> {
          val service = PostFixtures.postTestService()

          val result: Future[Seq[Post]] = service.listByUser(1)

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
        "must return an instance of Seq[Post]" >> {
          val service = PostFixtures.postTestService()

          val result: Future[Seq[Post]] = service.list()

          result must beAnInstanceOf[Seq[Post]].await
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
        "must return an instance of Seq[Post]" >> {
          val service = PostFixtures.postTestService()

          val result: Future[Seq[Post]] = service.listByAscendingOrder()

          result must beAnInstanceOf[Seq[Post]].await
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
        "must return an instance of Seq[Post]" >> {
          val service = PostFixtures.postTestService()

          val result: Future[Seq[Post]] = service.listByDescendingOrder()

          result must beAnInstanceOf[Seq[Post]].await
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
        "must return an instance of Post" >> {
          val post = Post(1, "randomtext", "randomPath", Timestamp.valueOf(LocalDateTime.now(defaultClock)), 1)
          val service = PostFixtures.postTestService(posts = ArrayBuffer(post))

          val result: Future[Post] = service.update(1, "random-text", "random-path", new File("/randompath"))

          result must beAnInstanceOf[Post].await
        }
        "must return PostNotFoundExeption" >> {
          val service = PostFixtures.postTestService()

          val result: Future[Post] = service.update(1, "random-text", "random-path", new File("/randompath"))

          result must throwA[PostNotFoundException].await
        }
      }
    }
  }

}

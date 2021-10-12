package services

import fixtures.{ClockFixture, UserFixtures}
import models.User.{EmailAddressAlreadyExistsException, InvalidEmailAddressException, InvalidUserNameCharactersException, User}
import org.specs2.mutable.Specification
import org.specs2.concurrent.ExecutionEnv
import repositories.users.UserRepository
import services.users.UserServiceImpl

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

class UserServiceSpec(implicit ev: ExecutionEnv) extends Specification {

  object TestFixture extends ClockFixture

  import TestFixture._

  case class TestData(Users: ArrayBuffer[User] = ArrayBuffer.empty)

  private def serviceWithRepoReturningException() = new UserServiceImpl(
    new UserRepository {
      override def register(user: User)(implicit ex: ExecutionContext): Future[User] = Future.failed(new Exception())

      override def findById(userId: Long)(implicit ex: ExecutionContext): Future[Option[User]] = Future.failed(new Exception())

      override def list()(implicit ex: ExecutionContext): Future[Seq[User]] = Future.failed(new Exception())
    }
  )

  "This is the specification of UserService" >> {
    "insert" >> {
      "must throw an exception" >> {
        "if some exception is thrown in the repository" >> {
          val result = serviceWithRepoReturningException()
            .register("randomname", "random@gmail.com")

          result must throwA[Exception].await
        }
        "must return an instance of User" >> {
          val user = User(1, "randomname", "larissaqueiroz.p@gmail.com")
          val service = UserFixtures.userTestService()

          val result: Future[User] = service.register(user.name, user.email)

          result must beAnInstanceOf[User].await
        }
        "if the email is invalid" >> {
          "must throw InvalidEmailAddressException" >> {
            val user = User(1, "randomname", "random32131")
            val service = UserFixtures.userTestService()

            val result: Future[User] = service.register(user.name, user.email)

            result must throwA[InvalidEmailAddressException].await
          }
        }
        "if the name is invalid" >> {
          "must throw InvalidUserNameCharactersException" >> {
            val user = User(1, "random21321", "random@gmail.com")
            val service = UserFixtures.userTestService()

            val result: Future[User] = service.register(user.name, user.email)

            result must throwA[InvalidUserNameCharactersException].await
          }
        }
        "if the email already exists" >> {
          "must throw EmailAddressAlreadyExistsException" >> {
            val user = User(1, "randomname", "random@gmail.com")
            val service = UserFixtures.userTestService(users = ArrayBuffer(user))

            val result: Future[User] = service.register(user.name, user.email)

            result must throwA[EmailAddressAlreadyExistsException].await
          }
        }
      }
    }
  }
}



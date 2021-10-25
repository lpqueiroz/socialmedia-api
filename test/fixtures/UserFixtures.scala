package fixtures

import components.model.IncrementId
import models.User.User
import repositories.users.UserRepository
import services.users.{UserService, UserServiceImpl}
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

object UserFixtures {

  def userTestService(
                       users: ArrayBuffer[User] = ArrayBuffer.empty
                     ) = new UserServiceImpl(userTestRepository(users))

  def userTestRepository(userData: ArrayBuffer[User]): UserRepository =
    new UserRepository with IncrementId {

      override def data: ArrayBuffer[User] = userData

      override def register(user: User)(implicit ex: ExecutionContext): Future[User] = Future.successful(user)

      override def findById(userId: Long)(implicit ex: ExecutionContext): Future[Option[User]] = Future.successful(userData.find(_.id == userId))

      override def list()(implicit ex: ExecutionContext): Future[Seq[User]] = Future.successful(userData)

      override def findByEmail(email: String)(implicit ex: ExecutionContext): Future[Option[User]] = Future.successful(userData.headOption)

    }

  def userStubService[A](result: Future[User]): UserService = new UserService {
    override def register(name: String, email: String)(implicit ex: ExecutionContext): Future[User] = result

  }
}

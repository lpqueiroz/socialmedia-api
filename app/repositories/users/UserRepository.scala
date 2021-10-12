package repositories.users

import com.google.inject.ImplementedBy
import models.User.User

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[UserDefaultRepository])
trait UserRepository {

  def register(user: User)(implicit ex: ExecutionContext): Future[User]

  def findById(userId: Long)(implicit ex: ExecutionContext): Future[Option[User]]

  def list()(implicit ex: ExecutionContext): Future[Seq[User]]

}

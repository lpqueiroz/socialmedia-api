package services.users

import com.google.inject.ImplementedBy
import models.User.User

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[UserServiceImpl])
trait UserService {

  def register(name: String, email: String)(implicit ex: ExecutionContext): Future[User]

}

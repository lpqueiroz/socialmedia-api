package services.users

import cats.data.Validated.{Invalid, Valid}
import javax.inject.Inject
import models.User.{EmailAddressAlreadyExistsException, User}
import repositories.users.UserRepository
import validation.UserValidator

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl @Inject()(val userRepository: UserRepository) extends UserService {

  override def register(name: String, email: String)(implicit ex: ExecutionContext): Future[User] = {
    for {
      userOpt <- userRepository.findByEmail(email)
      _ <- if (userOpt.nonEmpty) Future.failed(EmailAddressAlreadyExistsException(userOpt.get.email))
      else Future.successful(())
      validatedUser <- Future.successful(UserValidator.validateUser(name, email))
      user <- validatedUser match {
        case Invalid(e) => Future.failed(e)
        case Valid(a) => userRepository.register(a)
      }
    } yield user
  }

}

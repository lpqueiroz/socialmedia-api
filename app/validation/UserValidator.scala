package validation

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import models.User.{EmailAddressAlreadyExistsException, InvalidEmailAddressException, InvalidUserNameCharactersException, User, UserException}

object UserValidator {

  val NAME_REGEX = "^[a-zA-Z]+$"
  val EMAIL_REGEX = "^(.+)@(.+)$"

  private def validateName(name: String): Validated[InvalidUserNameCharactersException, String] = {
    if (name.matches(NAME_REGEX)) Valid(name) else Invalid(InvalidUserNameCharactersException(name))
  }

  private def validateEmail(email: String): Validated[InvalidEmailAddressException, String] = {
    if (email.matches(EMAIL_REGEX)) Valid(email) else Invalid(InvalidEmailAddressException(email))
  }

  def validateUser(name: String, email: String): Validated[UserException, User] = {
   validateName(name).andThen(validName =>
     validateEmail(email).map(validEmail =>
       User.buildUser(validName, validEmail)))
  }

}

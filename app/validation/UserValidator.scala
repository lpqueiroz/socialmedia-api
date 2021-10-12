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

  private def validateEmailIdentity(email: String): Validated[InvalidEmailAddressException, String] = {
    if (email.matches(EMAIL_REGEX)) Valid(email) else Invalid(InvalidEmailAddressException(email))
  }

  private def validateEmailExternal(email: String, emails: Seq[String]): Validated[EmailAddressAlreadyExistsException, String] = {
    if (!emails.contains(email)) Valid(email) else Invalid(EmailAddressAlreadyExistsException(email))
  }

  private def validateEmail(email: String, emails: Seq[String]): Validated[UserException, String] = {
    validateEmailIdentity(email).andThen(validEmail =>
      validateEmailExternal(validEmail, emails))
  }

  def validateUser(name: String, email: String, emails: Seq[String]): Validated[UserException, User] = {
   validateName(name).andThen(validName =>
     validateEmail(email, emails).map(validEmail =>
       User.buildUser(validName, validEmail)))
  }

}

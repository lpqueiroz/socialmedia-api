package models.User

class UserException(message: String) extends Exception(message)

case class UserNotFoundException() extends UserException(
  s"The User was not found"
)

case class EmailAddressAlreadyExistsException(email: String) extends UserException(
  s"The email address ${email} already exists."
)

case class InvalidUserNameCharactersException(name: String) extends UserException(
  s"The user's name ${name} has invalid characters. Only letters allowed."
)

case class InvalidEmailAddressException(email: String) extends UserException(
  s"The email ${email} is invalid."
)



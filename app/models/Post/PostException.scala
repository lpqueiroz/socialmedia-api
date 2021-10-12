package models.Post

class PostException(message: String) extends Exception(message)

case class PostNotFoundException() extends PostException(
  "The Post was not found."
)

case class UpdateNotAuthorizedException() extends PostException(
  "You don't have permission to update this post."
)

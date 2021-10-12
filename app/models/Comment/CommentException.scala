package models.Comment

class CommentException(message: String) extends Exception(message)

case class CommentNotFoundException() extends CommentException(
  "The Comment was not found"
)

case class UpdateNotAuthorizedException() extends CommentException(
  "You don't have permission to update the comment"
)


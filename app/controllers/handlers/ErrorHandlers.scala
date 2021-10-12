package controllers.handlers

import models.Comment.{CommentNotFoundException, UpdateNotAuthorizedException}
import models.Post.PostNotFoundException
import models.User.{EmailAddressAlreadyExistsException, InvalidEmailAddressException, InvalidUserNameCharactersException, UserNotFoundException}
import play.api.mvc.Result
import play.api.mvc.Results._

trait ErrorHandlers {

  def handleCommentExceptions: PartialFunction[Throwable, Result] = {
    case e: CommentNotFoundException =>
      NotFound(e.getMessage)
    case e: UpdateNotAuthorizedException =>
      Forbidden(e.getMessage)
  }

  def handlePostExceptions: PartialFunction[Throwable, Result] = {
    case e: PostNotFoundException =>
      NotFound(e.getMessage)
    case e: UpdateNotAuthorizedException =>
      Forbidden(e.getMessage)
  }

  def handleUserExceptions: PartialFunction[Throwable, Result] = {
    case e: UserNotFoundException =>
      NotFound(e.getMessage)
    case e: EmailAddressAlreadyExistsException =>
      Conflict(e.getMessage)
    case e: InvalidUserNameCharactersException =>
      BadRequest(e.getMessage)
    case e: InvalidEmailAddressException =>
      BadRequest(e.getMessage)
  }

  def handleDefaultExceptions: PartialFunction[Throwable, Result] = {
    case _ =>
      InternalServerError("An error ocurred")
  }

  def exceptionHandlers: PartialFunction[Throwable, Result] =
    handleCommentExceptions orElse
      handlePostExceptions orElse
      handleUserExceptions orElse
      handleDefaultExceptions

}

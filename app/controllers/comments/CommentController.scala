package controllers.comments

import java.time.Clock

import controllers.handlers.ErrorHandlers
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Request, _}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json, Writes}
import controllers.comments.CommentResource._
import services.comments.CommentService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}


class CommentController @Inject() (val commentService: CommentService,
                                   val controllerComponents: ControllerComponents)
                                    extends I18nSupport with
  BaseController with ErrorHandlers {

  implicit lazy val clock: Clock = Clock.systemDefaultZone()

  def insert(): Action[AnyContent] = Action.async {
    implicit request =>
      CommentForm.form.bindFromRequest.fold(
        errors => {
          println(errors)
          Future(BadRequest(errors.errorsAsJson))
        },
        form => commentService.insert(form.text, form.createdBy, form.postId)
          .map(comment => Ok(Json.toJson(comment))).recover(exceptionHandlers)
      )
  }

  def list(): Action[AnyContent] = Action.async {
    implicit request =>
      commentService
        .list()
        .map(
          comments => Ok(Json.toJson(comments))
        ).recover(exceptionHandlers)
  }

  def listByUser(userId: Long): Action[AnyContent] = Action.async {
    implicit request =>
      commentService
        .listByUser(userId)
        .map(
          comments => Ok(Json.toJson(comments))
        ).recover(exceptionHandlers)
  }

  def listByAscendingOrder(): Action[AnyContent] = Action.async {
    implicit request =>
      commentService
        .listByAscendingOrder()
        .map(
          comments => Ok(Json.toJson(comments))
        ).recover(exceptionHandlers)
  }

  def listByDescendingOrder(): Action[AnyContent] = Action.async {
    implicit request =>
      commentService
        .listByDescendingOrder()
        .map(
          comments => Ok(Json.toJson(comments))
        ).recover(exceptionHandlers)
  }

  def update(id: Long): Action[AnyContent] = Action.async {
    implicit request =>
      CommentForm.form.bindFromRequest.fold(
        errors => Future(BadRequest(errors.errorsAsJson)),
        form => commentService.update(id, form.text)
          .map(post => Ok(Json.toJson(post))).recover(exceptionHandlers)
      )
  }

  def listByPost(postId: Long): Action[AnyContent] = Action.async {
    implicit request =>
      commentService
        .listByPost(postId)
        .map(
          comments => Ok(Json.toJson(comments))
        ).recover(exceptionHandlers)
  }
}


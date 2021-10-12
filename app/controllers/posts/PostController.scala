package controllers.posts

import java.nio.file.{Path, Paths}
import java.time.Clock

import auth.AuthAction
import controllers.handlers.ErrorHandlers
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Request, _}
import models.Post.Post
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json, Writes}
import controllers.posts.PostResource._
import services.posts.PostService
import scala.concurrent.{ExecutionContext, Future}


class PostController @Inject() (val postService: PostService,
                                val controllerComponents: ControllerComponents,
                                val authAction: AuthAction)
                               (implicit ex: ExecutionContext) extends I18nSupport with
  BaseController with ErrorHandlers {

  implicit lazy val clock: Clock = Clock.systemDefaultZone()

  def insert(): Action[AnyContent] = Action.async {
    implicit request =>
      request.body.asMultipartFormData match {
        case Some(value) => PostForm.form.bindFromRequest.fold(
          errors => Future(BadRequest(errors.errorsAsJson)),
          form => {
            val post: Future[Post] = for {
              file <- Future.successful(value.files.head)
              insert <- postService.insert(form.text, file.ref, "images/" + file.filename, form.createdBy)
            } yield insert
            post.map(p => Ok(Json.toJson(p)))
          }
        )
        case None => Future.successful(BadRequest("File not found"))
      }
  }

  def list(): Action[AnyContent] = authAction.async {
    implicit request =>
      postService
        .list()
        .map(
          posts => Ok(Json.toJson(posts))
        ).recover(exceptionHandlers)
  }

  def listByUser(userId: Long): Action[AnyContent] = Action.async {
    implicit request =>
      postService
        .listByUser(userId)
        .map(
          posts => Ok(Json.toJson(posts))
        ).recover(exceptionHandlers)
  }

  def listByAscendingOrder(): Action[AnyContent] = Action.async {
    implicit request =>
      postService
        .listByAscendingOrder()
        .map(
          posts => Ok(Json.toJson(posts))
        ).recover(exceptionHandlers)
  }

  def listByDescendingOrder(): Action[AnyContent] = Action.async {
    implicit request =>
      postService
        .listByDescendingOrder()
        .map(
          posts => Ok(Json.toJson(posts))
        ).recover(exceptionHandlers)
  }

  def update(id: Long): Action[AnyContent] = Action.async { implicit request =>
    request.body.asMultipartFormData match {
      case Some(value) => PostForm.form.bindFromRequest.fold(
        errors => Future(BadRequest(errors.errorsAsJson)),
        form => {
          val post: Future[Post] = for {
            file <- Future.successful(value.files.head)
            update <- postService.update(id, form.text, "images/" + file.filename, file.ref)
          } yield update
          post.map(p => Ok(Json.toJson(p)))
        }
      )
      case None => Future.successful(BadRequest)
    }
  }

}

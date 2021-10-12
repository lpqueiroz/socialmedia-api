package controllers.users

import controllers.handlers.ErrorHandlers
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Request, _}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json, Writes}
import controllers.users.UserResource._
import services.users.{UserService, UserServiceImpl}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}


class UserController @Inject() (val userService: UserService,
                                val controllerComponents: ControllerComponents)
                               extends I18nSupport with
  BaseController with ErrorHandlers {


  def register(): Action[AnyContent] = Action.async {
    implicit request =>
      UserForm.form.bindFromRequest.fold(
        errors => Future(BadRequest(errors.errorsAsJson)),
        form => userService.register(form.name, form.email)
          .map(user => Ok(Json.toJson(user))).recover(exceptionHandlers)
      )
  }
}

package controllers.users

import models.User.User
import play.api.libs.json.Json

object UserResource {

    implicit val writesUser = Json.writes[User]

}

package controllers.comments

import models.Comment.Comment
import play.api.libs.json.Json

object CommentResource {

  implicit val writesUser = Json.writes[Comment]

}

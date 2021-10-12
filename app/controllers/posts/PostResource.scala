package controllers.posts

import models.Post.Post
import play.api.libs.json.Json


object PostResource {

   implicit val writesPost = Json.writes[Post]

}

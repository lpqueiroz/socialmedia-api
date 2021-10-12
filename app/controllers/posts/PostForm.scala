package controllers.posts

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}

object PostForm {

  case class PostForm(text: String, createdBy: Long)

  val form = Form(
    mapping(
      "text" -> nonEmptyText,
      "createdBy" -> longNumber
    )(PostForm.apply)(PostForm.unapply)
  )

}

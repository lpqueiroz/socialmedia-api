package controllers.comments

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText, optional}

object CommentForm {

  case class CommentForm(text: String, postId: Long, createdBy: Long)

  val form = Form(
    mapping(
      "text" -> nonEmptyText,
      "postId" -> longNumber,
      "createdBy" -> longNumber
    )(CommentForm.apply)(CommentForm.unapply)
  )

}

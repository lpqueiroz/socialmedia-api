package controllers.users

import play.api.data.Form
import play.api.data.Forms._

object UserForm {
  case class UserForm(name: String, email: String)

  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> nonEmptyText
    )(UserForm.apply)(UserForm.unapply)
  )
}


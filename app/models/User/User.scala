package models.User

case class User(
                 id: Long,
                 name: String,
                 email: String
               )

object User {
  def buildUser(
            name: String,
            email: String
          ) = apply(
    0,
    name,
    email
  )
}

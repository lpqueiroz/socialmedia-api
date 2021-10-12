package repositories.users

import models.User.User
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait UserComponent extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class Users(tag: Tag) extends Table[User](tag, "t_users"){

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Length(300, varying = true))

    def email = column[String]("email", O.Length(300, varying = true), O.Unique)

    override def * = (
      id,
      name,
      email,
    ) <> ((User.apply _).tupled, User.unapply)

  }
}


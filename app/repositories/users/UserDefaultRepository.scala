package repositories.users

import javax.inject.Inject
import models.User.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UserDefaultRepository @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider) extends
  UserRepository with UserComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val usersTable = TableQuery[Users]

  override def register(user: User)(implicit ex: ExecutionContext): Future[User] = {
    dbConfig.db.run(
      usersTable += user
    ).map(_ => user)
  }

  override def findById(userId: Long)(implicit ex: ExecutionContext): Future[Option[User]] = {
    dbConfig.db.run(
      usersTable.filter(_.id === userId).result.headOption
    )
  }

  override def list()(implicit ex: ExecutionContext): Future[Seq[User]] = {
    dbConfig.db.run(
      usersTable.result
    )
  }

  override def findByEmail(email: String)(implicit ex: ExecutionContext): Future[Option[User]] = {
    dbConfig.db.run(
      usersTable.filter(_.email === email).result.headOption
    )
  }
}

package repositories.posts

import java.sql.Timestamp
import java.time.{Clock, LocalDateTime}

import javax.inject.Inject
import models.Post.Post
import models.User.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class PostDefaultRepository @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider) extends
  PostRepository with PostComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val postsTable = TableQuery[Posts]

  override def list()(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    dbConfig.db.run(
      postsTable.result
    )
  }

  override def insert(post: Post)(implicit ex: ExecutionContext): Future[Post] = {
    dbConfig.db.run(
      postsTable += post
    ).map(_ => post)
  }

  override def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    dbConfig.db.run(
      postsTable.filter(_.createdBy === user.id).result
    )
  }

  override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    dbConfig.db.run(
      postsTable.sortBy(_.createdAt.asc).result
    )
  }

  override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    dbConfig.db.run(
      postsTable.sortBy(_.createdAt.desc).result
    )
  }

  override def update(post: Post)(implicit ex: ExecutionContext, clock: Clock): Future[Post] = {
    dbConfig.db.run(
      postsTable
        .filter(_.id === post.id)
        .map(p => (p.text, p.imagePath, p.createdAt, p.createdBy))
        .update((post.text, post.imagePath, Timestamp.valueOf(LocalDateTime.now(clock)), post.createdBy))
        .map(_ => post)
    )
  }

  override def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Post]] = {
    dbConfig.db.run(
      postsTable.filter(_.id === id).result.headOption
    )
  }

}

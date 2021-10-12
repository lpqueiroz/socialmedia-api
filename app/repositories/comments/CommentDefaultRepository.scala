package repositories.comments

import java.sql.Timestamp
import java.time.{Clock, LocalDateTime}

import javax.inject.Inject
import models.Comment.Comment
import models.Post.Post
import models.User.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CommentDefaultRepository @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider) extends
  CommentRepository with CommentComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val commentsTable = TableQuery[Comments]

  override def list()(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    dbConfig.db.run(
      commentsTable.result
    )
  }

  override def insert(comment: Comment)(implicit ex: ExecutionContext): Future[Comment] = {
    dbConfig.db.run(
      commentsTable += comment
    ).map(_ => comment)
  }

  override def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    dbConfig.db.run(
      commentsTable.filter(_.createdBy === user.id).result
    )
  }

  override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    dbConfig.db.run(
      commentsTable.sortBy(_.createdAt.asc).result
    )
  }

  override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    dbConfig.db.run(
      commentsTable.sortBy(_.createdAt.desc).result
    )
  }

  override def update(comment: Comment)(implicit ex: ExecutionContext, clock: Clock): Future[Comment] = {
    dbConfig.db.run(
      commentsTable
        .filter(_.id === comment.id)
        .map(p => (p.text, p.createdAt, p.createdBy))
        .update((comment.text, Timestamp.valueOf(LocalDateTime.now(clock)), comment.createdBy))
        .map(_ => comment)
    )
  }

  override def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Comment]] = {
    dbConfig.db.run(
      commentsTable.filter(_.id === id).result.headOption
    )
  }

  override def listByPost(post: Post)(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    dbConfig.db.run(
      commentsTable.filter(_.postId === post.id).result
    )
  }

}

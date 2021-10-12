package repositories.comments

import java.sql.Timestamp

import models.Comment.Comment
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile


trait CommentComponent extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class Comments(tag: Tag) extends Table[Comment](tag, "t_comments"){

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def text = column[String]("text", O.Length(300, varying = true))

    def createdAt = column[Timestamp]("created_at")

    def createdBy = column[Long]("created_by")

    def postId = column[Long]("post_id")

    override def * = (
      id,
      text,
      createdAt,
      createdBy,
      postId
    ) <> ((Comment.apply _).tupled, Comment.unapply)

  }
}


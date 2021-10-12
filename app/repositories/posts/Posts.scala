package repositories.posts

import java.sql.Timestamp

import models.Post.Post
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile


trait PostComponent extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class Posts(tag: Tag) extends Table[Post](tag, "t_posts"){

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def text = column[String]("text", O.Length(300, varying = true))

    def imagePath = column[String]("image", O.Length(300, varying = true))

    def createdAt = column[Timestamp]("created_at")

    def createdBy = column[Long]("created_by")

    override def * = (
      id,
      text,
      imagePath,
      createdAt,
      createdBy,
    ) <> ((Post.apply _).tupled, Post.unapply)

  }

}



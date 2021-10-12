package models.Post

import java.sql.Timestamp
import java.time.{Clock, LocalDateTime}

case class Post(
                 id: Long,
                 text: String,
                 imagePath: String,
                 createdAt: Timestamp,
                 createdBy: Long
               )

object Post {
  def buildPost(
                 text: String,
                 imagePath: String,
                 createdBy: Long
               )(implicit clock: Clock) = apply(
    0,
    text,
    imagePath,
    Timestamp.valueOf(LocalDateTime.now(clock)),
    createdBy
  )
}

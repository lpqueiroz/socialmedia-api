package models.Comment

import java.sql.Timestamp
import java.time.{Clock, LocalDateTime}

case class Comment(
                    id: Long,
                    text: String,
                    createdAt: Timestamp,
                    createdBy: Long,
                    postId: Long
                  )

object Comment {
  def buildComment(
                  text: String,
                  createdBy: Long,
                  postId: Long
               )(implicit clock: Clock) = apply(
    0,
    text,
    Timestamp.valueOf(LocalDateTime.now(clock)),
    createdBy,
    postId
  )
}

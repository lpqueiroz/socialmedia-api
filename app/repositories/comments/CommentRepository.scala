package repositories.comments

import java.time.Clock

import com.google.inject.ImplementedBy
import models.Comment.Comment
import models.Post.Post
import models.User.User
import repositories.posts.PostDefaultRepository

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[CommentDefaultRepository])
trait CommentRepository {

  def list()(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def insert(comment: Comment)(implicit ex: ExecutionContext): Future[Comment]

  def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def update(comment: Comment)(implicit ex: ExecutionContext, clock: Clock): Future[Comment]

  def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Comment]]

  def listByPost(post: Post)(implicit ex: ExecutionContext): Future[Seq[Comment]]

}

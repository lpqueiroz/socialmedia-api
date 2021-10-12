package repositories.posts

import java.time.Clock

import com.google.inject.ImplementedBy
import models.Post.Post
import models.User.User

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[PostDefaultRepository])
trait PostRepository {

  def list()(implicit ex: ExecutionContext): Future[Seq[Post]]

  def insert(post: Post)(implicit ex: ExecutionContext): Future[Post]

  def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Post]]

  def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]]

  def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]]

  def update(post: Post)(implicit ex: ExecutionContext, clock: Clock): Future[Post]

  def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Post]]

}

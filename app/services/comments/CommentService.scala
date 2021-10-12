package services.comments

import java.time.Clock

import com.google.inject.ImplementedBy
import models.Comment.Comment

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[CommentServiceImpl])
trait CommentService {

  def list()(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def insert(text: String, createdBy: Long, postId: Long)(implicit ex: ExecutionContext, clock: Clock): Future[Comment]

  def listByUser(userId: Long)(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]]

  def update(id: Long, text: String)(implicit ex: ExecutionContext, clock: Clock): Future[Comment]

  def listByPost(postId: Long)(implicit ex: ExecutionContext): Future[Seq[Comment]]

}

package repositories

import scala.concurrent.{ExecutionContext, Future}

// This trait can be used as a GenericRepository for both CommentRepository and PostRepository in the future to avoid duplication.
trait GenericRepository {

  def list[A]()(implicit ex: ExecutionContext): Future[Seq[A]]

  def insert[A](a: A)(implicit ex: ExecutionContext): Future[A]

  def listByUser[A, B](b: B)(implicit ex: ExecutionContext): Future[Seq[A]]

  def listByAscendingOrder[A]()(implicit ex: ExecutionContext): Future[Seq[A]]

  def listByDescendingOrder[A]()(implicit ex: ExecutionContext): Future[Seq[A]]

  def update[A](a: A)(implicit ex: ExecutionContext): Future[A]

  def findById[A](id: Long)(implicit ex: ExecutionContext): Future[Option[A]]
}

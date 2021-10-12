package services.posts

import java.io.File
import java.time.Clock

import com.google.inject.ImplementedBy
import models.Post.Post

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[PostServiceImpl])
trait PostService {

  def insert(text: String, imageFile: File, imagePath: String, createdBy: Long)
            (implicit ex: ExecutionContext, clock: Clock): Future[Post]

  def list()(implicit ex: ExecutionContext): Future[Seq[Post]]

  def listByUser(userId: Long)(implicit ex: ExecutionContext): Future[Seq[Post]]

  def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]]

  def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]]

  def update(id: Long, text: String, image: String)
            (implicit ex: ExecutionContext, clock: Clock): Future[Post]

}

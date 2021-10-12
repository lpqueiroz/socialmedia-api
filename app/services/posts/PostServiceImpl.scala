package services.posts

import java.io.File
import java.time.Clock

import javax.inject.Inject
import models.Post.{Post, PostNotFoundException}
import models.User.UserNotFoundException
import repositories.files.FileRepository
import repositories.posts.PostRepository
import repositories.users.UserRepository

import scala.concurrent.{ExecutionContext, Future}

class PostServiceImpl @Inject()(val postRepository: PostRepository,
                                val userRepository: UserRepository,
                                val fileRepository: FileRepository
                               ) extends PostService {

  override def insert(text: String, imageFile: File, imagePath: String, createdBy: Long)
            (implicit ex: ExecutionContext, clock: Clock): Future[Post] = {

    val uri = fileRepository.saveFile(imagePath, imageFile)
    val post: Post = Post.buildPost(text, uri.toString, createdBy)
    postRepository.insert(post)
  }

  override def list()(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    postRepository.list()
  }

  override def listByUser(userId: Long)(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    for {
      user <- userRepository.findById(userId)
      posts <- user match {
        case Some(user) => postRepository.listByUser(user)
        case _ => Future.failed(UserNotFoundException())
      }
    } yield posts
  }

  override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    postRepository.listByAscendingOrder()
  }

  override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = {
    postRepository.listByDescendingOrder()
  }

  override def update(id: Long, text: String, imagePath: String, imageFile: File)
            (implicit ex: ExecutionContext, clock: Clock): Future[Post] = {
    val uri = fileRepository.saveFile(imagePath, imageFile)
    for {
      postOpt <- postRepository.findById(id)
      updatePost <- postOpt match {
        case Some(post) => postRepository.update(
          Post(post.id, text, uri.toString, post.createdAt, post.createdBy)
        )
        case _ => Future.failed(PostNotFoundException())
      }
    } yield updatePost
  }

}

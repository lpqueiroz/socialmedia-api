package services.comments

import java.time.Clock

import javax.inject.Inject
import models.Comment.{Comment, CommentNotFoundException}
import models.Post.PostNotFoundException
import models.User.UserNotFoundException
import repositories.comments.CommentRepository
import repositories.posts.PostRepository
import repositories.users.UserRepository

import scala.concurrent.{ExecutionContext, Future}

class CommentServiceImpl @Inject()(val commentRepository: CommentRepository,
                                   val userRepository: UserRepository,
                                   val postRepository: PostRepository,
                              ) extends CommentService {

  override def list()(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    commentRepository.list()
  }

  override def insert(text: String, createdBy: Long, postId: Long)(implicit ex: ExecutionContext, clock: Clock): Future[Comment] = {
    val comment = Comment.buildComment(text, createdBy, postId)
    commentRepository.insert(comment)
  }

  override def listByUser(userId: Long)(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    for {
      user <- userRepository.findById(userId)
      comments <- user match {
        case Some(user) => commentRepository.listByUser(user)
        case _ => Future.failed(UserNotFoundException())
      }
    } yield comments
  }

  override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    commentRepository.listByAscendingOrder()
  }

  override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    commentRepository.listByDescendingOrder()
  }

  override def update(id: Long, text: String)(implicit ex: ExecutionContext, clock: Clock): Future[Comment] = {
    for {
      commentOpt <- commentRepository.findById(id)
      updateComment <- commentOpt match {
        case Some(comment) => commentRepository.update(
          Comment(comment.id, text, comment.createdAt, comment.createdBy, comment.postId))
        case _ => Future.failed(CommentNotFoundException())
      }
    } yield updateComment
  }

  override def listByPost(postId: Long)(implicit ex: ExecutionContext): Future[Seq[Comment]] = {
    for {
      post <- postRepository.findById(postId)
      comments <- post match {
        case Some(post) => commentRepository.listByPost(post)
        case _ => Future.failed(PostNotFoundException())
      }
    } yield comments
  }

}

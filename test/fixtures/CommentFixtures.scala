package fixtures

import java.time.Clock

import components.model.IncrementId
import models.Comment.{Comment, CommentNotFoundException}
import models.Post.Post
import models.User.User
import repositories.comments.CommentRepository
import services.comments.{CommentService, CommentServiceImpl}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


object CommentFixtures {

  def commentTestService(
                       users: ArrayBuffer[User] = ArrayBuffer.empty,
                       posts: ArrayBuffer[Post] = ArrayBuffer.empty,
                       comments: ArrayBuffer[Comment] = ArrayBuffer.empty
                     ) = new CommentServiceImpl(
    commentTestRepository(comments),
    UserFixtures.userTestRepository(users),
    PostFixtures.postTestRepository(posts)
  )

  def commentTestRepository(commentData: ArrayBuffer[Comment]): CommentRepository =
    new CommentRepository with IncrementId {

      override def data: ArrayBuffer[Comment] = commentData

      private def changeId(Comment: Comment, id: Long): Comment = Comment.copy(id = id)

      override def list()(implicit ex: ExecutionContext): Future[Seq[Comment]] = Future.successful(commentData)

      override def insert(Comment: Comment)(implicit ex: ExecutionContext): Future[Comment] =
        Future.fromTry(getNewId(id => Try(changeId(Comment, id))).map(Comment => commentData += Comment).map(_ => Comment))

      override def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Comment]] =
        Future.successful(commentData.filter(_.createdBy == user.id))

      override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] =
        Future.successful(commentData.sortBy(_.createdAt.getTime))

      override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] =
        Future.successful(commentData.sortWith(_.createdAt.getTime > _.createdAt.getTime))

      override def update(Comment: Comment)(implicit ex: ExecutionContext, clock: Clock): Future[Comment] =
        commentData.find(_.id.equals(Comment.id)).map {
        p => {
          commentData -= p
          commentData += Comment
          Future.successful(Comment)
        }
      }.getOrElse(Future.failed(CommentNotFoundException()))

      override def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Comment]] = Future.successful(commentData.find(_.id == id))

      override def listByPost(post: Post)(implicit ex: ExecutionContext): Future[Seq[Comment]] = Future.successful(commentData.filter(_.postId == post.id))
    }

  def commentStubService[A](resultComments: ArrayBuffer[Comment], result: Future[Seq[Comment]]): CommentService = new CommentService {
    override def list()(implicit ex: ExecutionContext): Future[Seq[Comment]] = result

    override def insert(text: String, createdBy: Long, postId: Long)(implicit ex: ExecutionContext, clock: Clock): Future[Comment] = result.map(_.head)

    override def listByUser(userId: Long)(implicit ex: ExecutionContext): Future[Seq[Comment]] = result

    override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = result

    override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Comment]] = result

    override def update(id: Long, text: String)(implicit ex: ExecutionContext, clock: Clock): Future[Comment] = result.map(_.head)

    override def listByPost(postId: Long)(implicit ex: ExecutionContext): Future[Seq[Comment]] = result
  }

}

package fixtures

import java.io.File
import java.time.Clock

import components.model.IncrementId
import models.Post.{Post, PostNotFoundException}
import models.User.User
import repositories.posts.PostRepository
import services.posts.{PostService, PostServiceImpl}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


object PostFixtures {

  def postTestService(
                      users: ArrayBuffer[User] = ArrayBuffer.empty,
                      fileData: ArrayBuffer[String]= ArrayBuffer.empty,
                      posts: ArrayBuffer[Post] = ArrayBuffer.empty
                     ) = new PostServiceImpl(postTestRepository(posts), UserFixtures.userTestRepository(users), FileFixtures.fileTestRepository(fileData))

  def postTestRepository(postData: ArrayBuffer[Post]): PostRepository =
    new PostRepository with IncrementId {

      override def data: ArrayBuffer[Post] = postData

      private def changeId(post: Post, id: Long): Post = post.copy(id = id)

      override def list()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.successful(postData)

      override def insert(post: Post)(implicit ex: ExecutionContext): Future[Post] =
        Future.fromTry(getNewId(id => Try(changeId(post, id))).map(post => postData += post).map(_ => post))

      override def listByUser(user: User)(implicit ex: ExecutionContext): Future[Seq[Post]] =
        Future.successful(postData.filter(_.createdBy == user.id))

      override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.successful(postData.sortBy(_.createdAt.getTime))

      override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = Future.successful(postData.sortWith(_.createdAt.getTime > _.createdAt.getTime))

      override def update(post: Post)(implicit ex: ExecutionContext, clock: Clock): Future[Post] = postData.find(_.id.equals(post.id)).map {
        p => {
          postData -= p
          postData += post
          Future.successful(post)
        }
      }.getOrElse(Future.failed(PostNotFoundException()))

      override def findById(id: Long)(implicit ex: ExecutionContext): Future[Option[Post]] = Future.successful(postData.find(_.id == id))
    }

  def postStubService[A](resultComments: ArrayBuffer[Post], result: Future[Seq[Post]]): PostService = new PostService {
    override def list()(implicit ex: ExecutionContext): Future[Seq[Post]] = result

    override def insert(text: String, imageFile: File, imagePath: String, createdBy: Long)(implicit ex: ExecutionContext, clock: Clock): Future[Post] = result.map(_.head)

    override def listByUser(userId: Long)(implicit ex: ExecutionContext): Future[Seq[Post]] = result

    override def listByAscendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = result

    override def listByDescendingOrder()(implicit ex: ExecutionContext): Future[Seq[Post]] = result

    override def update(id: Long, text: String, image: String, imageFile: File)(implicit ex: ExecutionContext, clock: Clock): Future[Post] = result.map(_.head)
  }

}

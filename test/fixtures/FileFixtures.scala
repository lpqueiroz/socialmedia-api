package fixtures

import java.io.File
import java.net.URI
import java.time.Clock

import components.model.IncrementId
import models.Post.{Post, PostNotFoundException}
import models.User.User
import repositories.files.FileRepository
import repositories.posts.PostRepository
import repositories.users.UserRepository
import services.users.UserServiceImpl

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


object FileFixtures {
  val BUCKET_NAME = "social-media-api-images"
  val REGION = "sa-east-1"


  def fileTestRepository(fileData: ArrayBuffer[String]): FileRepository =
    new FileRepository {

      override def saveFile(imagePath: String, imageFile: File) = URI.create(s"https://${BUCKET_NAME}.s3.${REGION}.amazonaws.com/${imagePath}")

    }
}
package repositories.files

import java.io.File
import java.net.URI

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.typesafe.config.Config
import javax.inject.Inject

class AWSFileRepository @Inject()(val config: Config) extends FileRepository {

  val BUCKET_NAME = "social-media-api-images"
  val REGION = "sa-east-1"
  val accessKey = config.getString("aws.accessKey")
  val secretKey = config.getString("aws.secretKey")
  val creds = new BasicAWSCredentials(accessKey, secretKey)
  val s3Client = AmazonS3ClientBuilder
    .standard()
    .withRegion("sa-east-1")
    .withCredentials(new AWSStaticCredentialsProvider(creds))
    .build()


  override def saveFile(imagePath: String, imageFile: File): URI = {
    s3Client.putObject(BUCKET_NAME, imagePath, imageFile)

    URI.create(s"https://${BUCKET_NAME}.s3.${REGION}.amazonaws.com/${imagePath}")
  }

}

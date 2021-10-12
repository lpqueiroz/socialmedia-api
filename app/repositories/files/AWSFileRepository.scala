package repositories.files

import java.io.File
import java.net.URI

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.typesafe.config.Config
import javax.inject.Inject

class AWSFileRepository @Inject()(config: Config) extends FileRepository {

  val BUCKET_NAME = "social-media-api-images"
  val REGION = "sa-east-1"
  val creds = new BasicAWSCredentials("AKIA4YVQUTEL6ITATTCU", "Qsbe4Cxx2FB9eSNXhhQud6DCO8dWVO3yV3FZsJNp")
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

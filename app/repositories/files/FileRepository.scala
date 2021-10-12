package repositories.files

import java.io.File
import java.net.URI

import com.google.inject.ImplementedBy

@ImplementedBy(classOf[AWSFileRepository])
trait FileRepository {

  def saveFile(imagePath: String, imageFile: File): URI
}

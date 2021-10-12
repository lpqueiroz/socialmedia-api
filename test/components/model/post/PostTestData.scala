package components.model.post

import models.Post.Post
import repositories.posts.PostRepository

import scala.collection.mutable.ArrayBuffer

trait PostTestData {

  val posts: ArrayBuffer[Post]

}

//trait PostRepositoryTestComponent {
//
//  val testData: PostTestData
//
//  lazy val postRepository: PostRepository =
//
//}

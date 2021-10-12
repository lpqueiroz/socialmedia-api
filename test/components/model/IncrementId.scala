package components.model

import scala.collection.mutable.ArrayBuffer
import scala.util.{Success, Try}

trait IncrementId {

  private var identity: Option[Long] = None

  def data: ArrayBuffer[_]

  private def getOrInitId[B](f: Long => Try[B]): Try[B] = identity match {
    case Some(x) => f(x)
    case None =>
      val newId = data.length + 1
      identity = Some(newId)
      f(newId)
  }

  protected def getNewId[B](f: Long => Try[B]): Try[B] = getOrInitId(f) match {
    case Success(value) => identity = identity.map(_ + 1)
      Try(value)
    case failure => failure
  }

}

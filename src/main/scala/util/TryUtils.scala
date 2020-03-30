package util

import scala.util.{Failure, Success, Try}

object TryUtils {

  def tryWith[A <: AutoCloseable, B](resource: A)(block: A => B): B = {
    Try(block(resource)) match {
      case Success(result) =>
        resource.close()
        result
      case Failure(exception) =>
        resource.close()
        throw exception
    }
  }

}

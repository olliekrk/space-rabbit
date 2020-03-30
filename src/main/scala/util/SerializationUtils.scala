package util

import org.json4s._
import org.json4s.native.Serialization

object SerializationUtils {

  implicit class SerializeOps[A](objectToSerialize: A)(implicit format: Formats) {
    def toJSON: String = Serialization.write(objectToSerialize)
  }

  def fromJSON[A](objectAsJson: String)(implicit manifest: Manifest[A], format: Formats): A =
    Serialization.read[A](objectAsJson)

}


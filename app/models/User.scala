package models

import java.util.UUID
import play.api.libs.json._
import org.reactivecouchbase.client.OpResult
import org.reactivecouchbase.play._
import play.api.Play.current
import scala.concurrent.Future

case class User (username: String,
                 password: String,
                 activated: Boolean){
  def save(): Future[OpResult] = User.save(this)
  def remove(): Future[OpResult] = User.remove(this)
}

object Users {
  implicit val userFmt = Json.format[User]

  def bucket = PlayCouchbase.bucket("default")

  def save(user: User): Future[OpResult] = {
    bucket.set[User](generateId(user), user)
  }

  private def generateId(user: User): String = {
    UUID.randomUUID().toString
  }
}

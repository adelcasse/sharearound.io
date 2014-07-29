package controllers

import play.api.mvc._
import play.api.libs.json._
import org.reactivecouchbase.play._
import play.api.Play.current
import models.User
import models.Users
import scala.concurrent.Future

object UsersController extends Controller {

  import models.Users.userFmt
  implicit val ec = PlayCouchbase.couchbaseExecutor

  def createUser() = Action(parse.json) { request =>
    request.body.validate[User].map {
      user => Users.save(user).map { status =>
        status.isSuccess match {
          case true => Ok(
            Json.obj(
              "status" -> "created",
              "error" -> false,
              "created" -> true,
              "message" -> status.getMessage,
              "user" -> user
            )
          )
          case false => BadRequest(
            Json.obj(
              "status" -> "error",
              "error" -> true,
              "created" -> false,
              "message" -> status.getMessage
            )
          )
        }
      }
    }
  }
}

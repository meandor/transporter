package de.transporter.example

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import de.transporter.platform.Platform.{ActionPerformed, Beam}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait ExampleRoutes extends JsonSupport {

  implicit def system: ActorSystem

  def actors: Map[String, ActorRef]

  implicit lazy val timeout = Timeout(5.seconds)

  lazy val exampleRoutes: Route =
    pathPrefix("example") {
      concat(
        pathEnd {
          concat(
            post {
              entity(as[ExampleRequest]) { testRequest =>
                val userCreated: Future[ActionPerformed] = {
                  val examplePadActor = actors.get("examplePad")
                  if (examplePadActor.isDefined) {
                    (examplePadActor.get ? Beam(testRequest.matter, testRequest.location)).mapTo[ActionPerformed]
                  } else {
                    Future(ActionPerformed("Actor not found!"))(ExecutionContext.global)
                  }
                }
                onSuccess(userCreated) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            }
          )
        }
      )
    }
}

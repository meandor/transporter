package de.transporter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import io.prometheus.client.hotspot.DefaultExports

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Transporter extends App with LazyLogging {
  logger.info("Starting Transporter")
  logger.debug("Loading config")
  val config = ConfigFactory.load()

  logger.debug("Starting Actor System")
  implicit val system: ActorSystem = ActorSystem("Transporter")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  logger.debug("Initializing standard metrics")
  DefaultExports.initialize()

  val httpConfig = config.getConfig("http")
  val httpPort = httpConfig.getInt("port")
  val httpInterface = httpConfig.getString("interface")
  val serverBindingFuture = Http().bindAndHandle(TransporterConsole.routes, interface = httpInterface, port = httpPort)
  logger.info(s"Started HTTP Server at $httpInterface:$httpPort")
  logger.info("Press RETURN to stop")
  StdIn.readLine()

  serverBindingFuture
    .flatMap(_.unbind())
    .onComplete { done =>
      done.failed.map { ex => logger.error("Failed unbinding", ex) }
      system.terminate()
    }
}

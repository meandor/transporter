package de.transporter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import io.prometheus.client.hotspot.DefaultExports
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Transporter extends App {
  val log = LoggerFactory.getLogger(Transporter.getClass)
  log.info("Starting Transporter")
  log.debug("Loading config")
  val config = ConfigFactory.load()

  log.debug("Starting Actor System")
  implicit val system: ActorSystem = ActorSystem("Transporter")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  log.debug("Initializing standard metrics")
  DefaultExports.initialize()

  val httpConfig = config.getConfig("http")
  val httpPort = httpConfig.getInt("port")
  val httpInterface = httpConfig.getString("interface")
  val serverBindingFuture = Http().bindAndHandle(TransporterConsole.routes, interface = httpInterface, port = httpPort)
  log.info(s"Started HTTP Server at $httpInterface:$httpPort")
  log.info("Press RETURN to stop")
  StdIn.readLine()

  serverBindingFuture
    .flatMap(_.unbind())
    .onComplete { done =>
      done.failed.map { ex => log.error("Failed unbinding", ex) }
      system.terminate()
    }
}

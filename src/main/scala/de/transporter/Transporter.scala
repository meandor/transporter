package de.transporter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import de.transporter.example.{ExamplePTC, ExampleTS}
import de.transporter.platform.Platform
import io.prometheus.client.hotspot.DefaultExports

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Transporter extends App with LazyLogging {
  logger.info("Starting Transporter")
  logger.debug("Loading config")

  val config = if (args.length > 0) {
    ConfigFactory.load(args(0))
  } else {
    ConfigFactory.load()
  }

  logger.debug("Starting Actor System")
  implicit val system: ActorSystem = ActorSystem("Transporter")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  // Register your actors here
  val testPad = system.actorOf(Platform.props(ExamplePTC, ExampleTS), "testPad")
  val actors = Map("examplePad" -> testPad)

  logger.debug("Initializing standard metrics")
  DefaultExports.initialize()

  val httpConfig = config.getConfig("http")
  val httpPort = httpConfig.getInt("port")
  val httpInterface = httpConfig.getString("interface")
  val transporterConsole = TransporterConsole(system, actors)
  val serverBindingFuture = Http().bindAndHandle(transporterConsole.routes, interface = httpInterface, port = httpPort)

  logger.info(s"Started HTTP Server at $httpInterface:$httpPort")
  logger.info("Enter quit to stop")

  var continue = true
  while (continue) {
    StdIn.readLine match {
      case "quit" => logger.info("Shutting down"); continue = false
      case x =>
    }
  }

  serverBindingFuture
    .flatMap(_.unbind())
    .onComplete { done =>
      done.failed.map { ex => logger.error("Failed unbinding", ex) }
      system.terminate()
    }
}

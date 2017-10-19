package de.transporter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import io.prometheus.client.hotspot.DefaultExports
import org.slf4j.LoggerFactory

object Transporter extends App {
  val log = LoggerFactory.getLogger(Transporter.getClass)
  log.info("Starting Transporter")
  log.debug("Loading config")
  val config = ConfigFactory.load().getConfig("http")

  log.debug("Starting Actor System")
  implicit val system: ActorSystem = ActorSystem("Transporter")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  log.debug("Initializing standard metrics")
  DefaultExports.initialize()

  log.info("Starting HTTP Server")
  val server = Http().bindAndHandle(TransporterConsole.routes, interface = config.getString("interface"), port = config.getInt("port"))
}

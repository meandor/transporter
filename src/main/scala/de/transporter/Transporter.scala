package de.transporter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import io.prometheus.client.hotspot.DefaultExports

object Transporter extends App {

  val config = ConfigFactory.load().getConfig("http")

  implicit val system: ActorSystem = ActorSystem("Transporter")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  DefaultExports.initialize()

  val server = Http().bindAndHandle(TransporterConsole.routes, interface = config.getString("interface"), port = config.getInt("port"))
}

package de.transporter

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.lonelyplanet.prometheus.PrometheusResponseTimeRecorder
import com.lonelyplanet.prometheus.api.MetricsEndpoint
import com.typesafe.scalalogging.LazyLogging
import de.transporter.example.ExampleRoutes
import io.prometheus.client.CollectorRegistry

case class TransporterConsole(system: ActorSystem, actors: Map[String, ActorRef]) extends ExampleRoutes with LazyLogging {
  private val prometheusRegistry: CollectorRegistry = PrometheusResponseTimeRecorder.DefaultRegistry

  private val prometheusResponseTimeRecorder: PrometheusResponseTimeRecorder = PrometheusResponseTimeRecorder.Default

  private val metricsEndpoint = new MetricsEndpoint(prometheusRegistry)

  val routes: Route = metricsEndpoint.routes ~ exampleRoutes
}

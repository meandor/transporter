package de.transporter.console

import akka.http.scaladsl.server.Route
import com.lonelyplanet.prometheus.PrometheusResponseTimeRecorder
import com.lonelyplanet.prometheus.api.MetricsEndpoint
import io.prometheus.client.CollectorRegistry

object TransporterConsole {
  private val prometheusRegistry: CollectorRegistry = PrometheusResponseTimeRecorder.DefaultRegistry

  private val prometheusResponseTimeRecorder: PrometheusResponseTimeRecorder = PrometheusResponseTimeRecorder.Default

  private val metricsEndpoint = new MetricsEndpoint(prometheusRegistry)

  val routes: Route = metricsEndpoint.routes
}

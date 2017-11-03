package com.github.meandor.transporter

import io.prometheus.client.{Counter, Gauge, Histogram}

/**
  * All the Metrics
  */
object Metrics {
  private val totalBeams: Counter = Counter.build().name("beams_total")
    .help("Total number of beams .").labelNames("status", "location", "owner", "matter").register()

  val beamLag: Gauge = Gauge.build().name("beam_lag")
    .help("Lag of unprocessed beams").labelNames("location").register()

  val materializationHistogram: Histogram = Histogram.build().name("materializations_latency_seconds")
    .help("Request latency in seconds.").labelNames("location").register()

  def successfulBeam(location: String, owner: String, matter: String): Unit = {
    totalBeams.labels("successful", location, owner, matter).inc()
  }

  def unsuccessfulBeam(location: String, owner: String, matter: String): Unit = {
    totalBeams.labels("unsuccessful", location, owner, matter).inc()
  }

  def faultyBeam(location: String, owner: String, matter: String): Unit = {
    totalBeams.labels("faulty", location, owner, matter).inc()
  }
}

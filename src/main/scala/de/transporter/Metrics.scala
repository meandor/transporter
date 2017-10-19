package de.transporter

import io.prometheus.client.Counter

object Metrics {
  private val successfulBeams: Counter = Counter.build().name("successful_beams_total")
    .help("Total number of successful beams .").labelNames("location", "owner", "matter").register()

  private val unsuccessfulBeams: Counter = Counter.build().name("unsuccessful_beams_total")
    .help("Total number of unsuccessful beams.").labelNames("location", "owner", "matter").register()

  private val faultyBeams: Counter = Counter.build().name("faulty_beams_total")
    .help("Total number of beams that went wrong.").labelNames("location", "owner", "matter").register()

  def successfulBeam(location: String, owner: String, matter: String): Unit = {
    successfulBeams.labels(location, owner, matter).inc()
  }

  def unsuccessfulBeam(location: String, owner: String, matter: String): Unit = {
    unsuccessfulBeams.labels(location, owner, matter).inc()
  }

  def faultyBeam(location: String, owner: String, matter: String): Unit = {
    faultyBeams.labels(location, owner, matter).inc()
  }
}

package de.transporter.platform

import de.transporter.{Location, Matter}
import io.prometheus.client.Counter

case class TransporterPad(ts: TargetingScanner, ptc: PhaseTransitionCoil) {
  val successfulBeams: Counter = Counter.build().name("successful_beams_total")
    .help("Total number of successful beams .").labelNames("location", "owner", "matter").register()

  val unsuccessfulBeams: Counter = Counter.build().name("unsuccessful_beams_total")
    .help("Total number of unsuccessful beams.").labelNames("location", "owner", "matter").register()

  val faultyBeams: Counter = Counter.build().name("faulty_beams_total")
    .help("Total number of beams that went wrong.").labelNames("location", "owner", "matter").register()

  def beam(subject: Matter, location: Location): Boolean = {
    val energy = ptc.energize(subject)
    val target = ts.lockOn(location)

    if (target.isDefined) {
      if (target.get.materialize(energy)) {
        successfulBeams.labels(location.id, subject.owner, subject.id).inc()
        return true
      } else {
        unsuccessfulBeams.labels(location.id, subject.owner, subject.id).inc()
        return false
      }
    }

    faultyBeams.labels(location.id, subject.owner, subject.id).inc()
    false
  }
}

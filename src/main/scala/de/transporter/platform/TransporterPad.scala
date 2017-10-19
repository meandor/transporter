package de.transporter.platform

import de.transporter.{Location, Matter}

case class TransporterPad(ts: TargetingScanner, ptc: PhaseTransitionCoil) {

  def beam(subject: Matter, location: Location): Boolean = {
    val energy = ptc.energize(subject)
    val target = ts.lockOn(location)

    if (target.isDefined) {
      return target.get.materialize(energy)
    }
    false
  }
}

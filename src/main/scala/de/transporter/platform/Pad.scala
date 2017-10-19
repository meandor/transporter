package de.transporter.platform

import de.transporter.Matter

case class Pad(target: Target, ptc: PhaseTransitionCoil) {

  def beam(subject: Matter): Boolean = {
    val energy = ptc.energize(subject)
    target.materialize(energy)
  }
}

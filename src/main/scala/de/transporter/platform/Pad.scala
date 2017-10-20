package de.transporter.platform

import de.transporter.{Matter, Metrics}

case class Pad(target: Target, ptc: PhaseTransitionCoil) {

  def beam(subject: Matter): Boolean = {
    val energy = ptc.energize(subject)

    val materializationTimer = Metrics.materializationHistogram.labels(target.location.id).startTimer
    try {
      target.materialize(energy)
    } finally {
      materializationTimer.observeDuration
    }
  }
}

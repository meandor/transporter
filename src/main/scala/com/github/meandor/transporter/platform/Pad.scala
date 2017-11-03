package com.github.meandor.transporter.platform

import com.github.meandor.transporter.{Matter, Metrics}

/**
  * Pad that does the actual transporting.
  *
  * @param target Target to be beamed to
  * @param ptc    PhaseTransitionCoil used for turning Matter to Energy
  */
case class Pad(target: Target, ptc: PhaseTransitionCoil) {

  /**
    * Actual beaming process for transporting Matter to a Location
    *
    * @param subject Matter to be beamed
    * @return Whether beaming was successful or not
    */
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

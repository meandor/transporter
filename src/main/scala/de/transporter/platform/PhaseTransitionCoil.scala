package de.transporter.platform

import de.transporter.Matter

trait PhaseTransitionCoil {
  def energize(subject: Matter): Energy
}

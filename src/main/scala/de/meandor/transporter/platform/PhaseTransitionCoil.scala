package de.meandor.transporter.platform

import de.meandor.transporter.Matter

trait PhaseTransitionCoil {
  def energize(subject: Matter): Energy
}

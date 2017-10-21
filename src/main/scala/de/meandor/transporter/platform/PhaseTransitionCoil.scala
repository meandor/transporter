package de.meandor.transporter.platform

import de.meandor.transporter.Matter

/**
  * Transforms Matter to Energy.
  *
  * E.g. Entity -> DTO, incoming data -> outgoing data
  */
trait PhaseTransitionCoil {

  /**
    * Transforms Matter to Energy
    *
    * @param subject Matter to be transformed
    * @return Transformed Energy
    */
  def energize(subject: Matter): Energy
}

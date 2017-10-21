package de.meandor.transporter.platform

import de.meandor.transporter.Location

/**
  * Materializes the energy at the location.
  *
  * Responsible for sending data to locations. Should implement API calls.
  */
trait Target {
  def location: Location

  /**
    * Materializes the energy at the given location.
    *
    * @param energy Energy to be materialized
    * @return @code{true} if successfully materialized, otherwise @code{false}
    */
  def materialize(energy: Energy): Boolean
}

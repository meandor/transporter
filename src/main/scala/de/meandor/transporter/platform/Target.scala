package de.meandor.transporter.platform

import de.meandor.transporter.Location

trait Target {
  def location: Location

  def materialize(energy: Energy): Boolean
}

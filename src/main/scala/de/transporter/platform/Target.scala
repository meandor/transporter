package de.transporter.platform

import de.transporter.Location

trait Target {
  def location: Location

  def materialize(energy: Energy): Boolean
}

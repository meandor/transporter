package de.transporter.platform

trait Target {
  def materialize(energy: Energy): Boolean
}

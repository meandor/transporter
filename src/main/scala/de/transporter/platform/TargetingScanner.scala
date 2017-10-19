package de.transporter.platform

import de.transporter.Location

trait TargetingScanner {
  def lockOn(location: Location): Option[Target]
}

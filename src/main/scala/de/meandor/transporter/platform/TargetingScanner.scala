package de.meandor.transporter.platform

import de.meandor.transporter.Location

trait TargetingScanner {
  def lockOn(location: Location): Option[Target]
}

package com.github.meandor.transporter.platform

import com.github.meandor.transporter.Location

/**
  * Finds the correct Target for a Location.
  *
  * Responsible for finding the correct service to be used to send data to a location.
  */
trait TargetingScanner {
  def lockOn(location: Location): Option[Target]
}

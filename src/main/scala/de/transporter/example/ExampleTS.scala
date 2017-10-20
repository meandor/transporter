package de.transporter.example

import com.typesafe.scalalogging.LazyLogging
import de.transporter.Location
import de.transporter.platform.{Energy, Target, TargetingScanner}

object ExampleTarget extends Target with LazyLogging {
  override def materialize(energy: Energy): Boolean = {
    logger.info(s"materializing: ${energy.toJson}")
    energy == ExampleEnergy(ExampleMatter("foo", "bar"))
  }
}

object ExampleTS extends TargetingScanner with LazyLogging {
  override def lockOn(location: Location): Option[Target] = {
    logger.info(s"targeting location: ${location.id}")
    if (location == ExampleLocation("uranus")) {
      Option(ExampleTarget)
    } else {
      None
    }
  }
}

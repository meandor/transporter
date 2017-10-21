package de.meandor.transporter.example

import com.typesafe.scalalogging.LazyLogging
import de.meandor.transporter.Matter
import de.meandor.transporter.platform.{Energy, PhaseTransitionCoil}

case class ExampleEnergy(subject: Matter) extends Energy {
  override def toJson: String = s"{'id':'${subject.id}','owner':'${subject.owner}'}"
}

object ExamplePTC extends PhaseTransitionCoil with LazyLogging {
  override def energize(subject: Matter): Energy = {
    logger.info(s"Energizin PhaseTransitionCoil with: ${subject.id}, ${subject.owner}")
    ExampleEnergy(subject)
  }
}

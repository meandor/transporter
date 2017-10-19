package de.transporter.platform

import de.transporter.{Location, Matter}
import org.scalatest.{FeatureSpec, Matchers}

class TransporterPadTest extends FeatureSpec with Matchers {

  feature("Use TransportPad to transport Matter to a Location") {
    scenario("Basic functionality and components test by transporting something to somewhere") {

      object TestMatter extends Matter {
        override def id = "foo"

        override def owner = "bar"
      }

      object TestLocation extends Location

      object TestEnergy extends Energy {
        override def toJson = "dematerialized energy"
      }

      object TestTarget extends Target {
        override def materialize(energy: Energy): Boolean = {
          energy shouldBe TestEnergy
          energy.toJson == "dematerialized energy"
        }
      }

      object TestTS extends TargetingScanner {
        override def lockOn(location: Location): Option[Target] = {
          location shouldBe TestLocation
          Option(TestTarget)
        }
      }

      object TestPTC extends PhaseTransitionCoil {
        override def energize(subject: Matter): Energy = {
          subject shouldBe TestMatter
          TestEnergy
        }
      }

      val pad = TransporterPad(TestTS, TestPTC)

      pad.beam(TestMatter, TestLocation) shouldBe true
    }
  }
}

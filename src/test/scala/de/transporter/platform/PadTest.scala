package de.transporter.platform

import de.transporter.Matter
import org.scalatest.{FeatureSpec, Matchers}

class PadTest extends FeatureSpec with Matchers {

  feature("Use TransportPad to transport Matter to a Location") {

    object TestMatter extends Matter {
      override def id = "foo"

      override def owner = "bar"
    }

    object TestEnergy extends Energy {
      override def toJson = "dematerialized energy"
    }

    object TestPTC extends PhaseTransitionCoil {
      override def energize(subject: Matter): Energy = {
        subject shouldBe TestMatter
        TestEnergy
      }
    }

    scenario("Transporting something to somewhere") {
      object TestTarget extends Target {
        override def materialize(energy: Energy): Boolean = {
          energy shouldBe TestEnergy
          energy.toJson == "dematerialized energy"
        }
      }

      Pad(TestTarget, TestPTC).beam(TestMatter) shouldBe true
    }

    scenario("Transporting something to nowhere") {
      object TestTarget extends Target {
        override def materialize(energy: Energy): Boolean = {
          energy shouldBe TestEnergy
          false
        }
      }

      Pad(TestTarget, TestPTC).beam(TestMatter) shouldBe false
    }
  }
}

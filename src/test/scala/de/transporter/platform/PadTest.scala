package de.transporter.platform

import org.scalatest.{FeatureSpec, Matchers}

class PadTest extends FeatureSpec with Matchers with TestSetup {
  feature("Use TransportPad to transport Matter to a Location") {
    scenario("Transporting something to somewhere") {
      Pad(ValidatingTestTarget, TestPTC).beam(ValidTestMatter) shouldBe true
    }

    scenario("Not transporting invalid something to somewhere") {
      Pad(ValidatingTestTarget, TestPTC).beam(InvalidTestMatter) shouldBe false
    }


    scenario("Transporting something to nowhere") {
      Pad(FailingTestTarget, TestPTC).beam(ValidTestMatter) shouldBe false
    }
  }
}

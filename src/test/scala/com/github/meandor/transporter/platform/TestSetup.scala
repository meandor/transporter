package com.github.meandor.transporter.platform

import com.github.meandor.transporter.{Location, Matter}

trait TestSetup {

  object ValidTestMatter extends Matter {
    override def id = "foo"

    override def owner = "bar"
  }

  object InvalidTestMatter extends Matter {
    override def id = "bar"

    override def owner = "foo"
  }

  case class TestEnergy(id: String) extends Energy {
    override def toJson = s"$id:dematerialized energy"
  }

  object TestPTC extends PhaseTransitionCoil {
    override def energize(subject: Matter): Energy = {
      TestEnergy(subject.id)
    }
  }

  object TestLocation extends Location {
    override def id = "foo:bar"
  }

  object ValidatingTestTarget extends Target {
    override def materialize(energy: Energy): Boolean = {
      energy.toJson == "foo:dematerialized energy"
    }

    override def location: Location = TestLocation
  }

  object FailingTestTarget extends Target {
    override def materialize(energy: Energy): Boolean = {
      false
    }

    override def location: Location = TestLocation
  }

}

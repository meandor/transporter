package de.transporter.platform

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import com.typesafe.scalalogging.LazyLogging
import de.transporter.platform.Platform.{ActionPerformed, Beam}
import de.transporter.{Location, Matter}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class PlatformTest(_system: ActorSystem) extends TestKit(_system) with Matchers with FlatSpecLike with BeforeAndAfterAll with LazyLogging {

  def this() = this(ActorSystem("PlatformTest"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  object TestMatter extends Matter {
    override def id = "foo"

    override def owner = "bar"
  }

  object TestLocation extends Location {
    override def id: String = "foobar"
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

  "A correct Platform Actor" should "successfully beam a subject to a location" in {
    object TestTarget extends Target {
      override def materialize(energy: Energy): Boolean = {
        energy shouldBe TestEnergy
        true
      }
    }

    object TestTS extends TargetingScanner {
      override def lockOn(location: Location): Option[Target] = {
        location shouldBe TestLocation
        Option(TestTarget)
      }
    }

    val testProbe = TestProbe()
    val platformActor = system.actorOf(Platform.props(TestPTC, TestTS))
    testProbe.send(platformActor, Beam(TestMatter, TestLocation))
    testProbe.expectMsg(ActionPerformed("Successful Beam"))
  }

  "A failing Platform Actor" should "unsuccessfully beam a subject to a location" in {
    object TestTarget extends Target {
      override def materialize(energy: Energy): Boolean = {
        energy shouldBe TestEnergy
        false
      }
    }

    object TestTS extends TargetingScanner {
      override def lockOn(location: Location): Option[Target] = {
        location shouldBe TestLocation
        Option(TestTarget)
      }
    }

    val testProbe = TestProbe()
    val platformActor = system.actorOf(Platform.props(TestPTC, TestTS))
    testProbe.send(platformActor, Beam(TestMatter, TestLocation))
    testProbe.expectMsg(ActionPerformed("Unsuccessful Beam"))
  }

  "A faulty Platform Actor" should "faulty beam a subject to a location" in {
    object TestTarget extends Target {
      override def materialize(energy: Energy): Boolean = {
        energy shouldBe TestEnergy
        true
      }
    }

    object TestTS extends TargetingScanner {
      override def lockOn(location: Location): Option[Target] = {
        location shouldBe TestLocation
        None
      }
    }

    val testProbe = TestProbe()
    val platformActor = system.actorOf(Platform.props(TestPTC, TestTS))
    testProbe.send(platformActor, Beam(TestMatter, TestLocation))
    testProbe.expectMsg(ActionPerformed("Faulty Beam, can not calibrate location"))
  }
}

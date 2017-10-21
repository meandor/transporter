package de.meandor.transporter.platform

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import de.meandor.transporter.Location
import de.meandor.transporter.platform.Platform.{ActionPerformed, Beam}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class PlatformTest(_system: ActorSystem) extends TestKit(_system)
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll
  with TestSetup {

  def this() = this(ActorSystem("PlatformTest"))

  override def afterAll: Unit = {
    shutdown(system)
  }

  "A correct Platform Actor" should "successfully beam a subject to a location" in {
    object TestTS extends TargetingScanner {
      override def lockOn(location: Location): Option[Target] = {
        location shouldBe TestLocation
        Option(ValidatingTestTarget)
      }
    }

    val testProbe = TestProbe()
    val platformActor = system.actorOf(Platform.props(TestPTC, TestTS))
    testProbe.send(platformActor, Beam(ValidTestMatter, TestLocation))
    testProbe.expectMsg(ActionPerformed("Successful Beam"))
  }

  "A failing Platform Actor" should "unsuccessfully beam a subject to a location" in {
    object TestTS extends TargetingScanner {
      override def lockOn(location: Location): Option[Target] = {
        location shouldBe TestLocation
        Option(ValidatingTestTarget)
      }
    }

    val testProbe = TestProbe()
    val platformActor = system.actorOf(Platform.props(TestPTC, TestTS))
    testProbe.send(platformActor, Beam(InvalidTestMatter, TestLocation))
    testProbe.expectMsg(ActionPerformed("Unsuccessful Beam"))
  }

  "A faulty Platform Actor" should "faulty beam a subject to a location" in {
    object TestTS extends TargetingScanner {
      override def lockOn(location: Location): Option[Target] = {
        location shouldBe TestLocation
        None
      }
    }

    val testProbe = TestProbe()
    val platformActor = system.actorOf(Platform.props(TestPTC, TestTS))
    testProbe.send(platformActor, Beam(ValidTestMatter, TestLocation))
    testProbe.expectMsg(ActionPerformed("Faulty Beam, can not calibrate location"))

    testProbe.send(platformActor, Beam(InvalidTestMatter, TestLocation))
    testProbe.expectMsg(ActionPerformed("Faulty Beam, can not calibrate location"))
  }
}

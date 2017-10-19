package de.transporter.platform

import akka.actor.{Actor, ActorLogging, Props}
import de.transporter.{Location, Matter, Metrics}

object Platform {

  final case class Beam(subject: Matter, location: Location)

  final case class ActionPerformed(description: String)

  def props(ptc: PhaseTransitionCoil, ts: TargetingScanner): Props = Props(new Platform(ptc, ts))
}

class Platform(ptc: PhaseTransitionCoil, ts: TargetingScanner) extends Actor with ActorLogging {

  import Platform._

  def receive: Receive = {
    case Beam(subject, location) =>
      val target = ts.lockOn(location)
      if (target.isDefined) {
        if (Pad(target.get, ptc).beam(subject)) {
          Metrics.successfulBeam(location.id, subject.owner, subject.id)
          sender() ! ActionPerformed(s"Successful Beam")
        } else {
          Metrics.unsuccessfulBeam(location.id, subject.owner, subject.id)
          sender() ! ActionPerformed(s"Unsuccessful Beam")
        }
      } else {
        Metrics.faultyBeam(location.id, subject.owner, subject.id)
        sender() ! ActionPerformed(s"Faulty Beam, can not calibrate location")
      }
  }
}

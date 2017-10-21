package de.meandor.transporter.platform

import akka.actor.{Actor, ActorLogging, Props}
import de.meandor.transporter.{Location, Matter, Metrics}

/**
  * Actor object for beaming (sending) subjects (data) to a Location.
  */
object Platform {

  /**
    * Incoming Beam request via internal REST-API.
    *
    * @param subject  Matter to be beamed
    * @param location Location to be beamed to
    */
  final case class Beam(subject: Matter, location: Location)

  /**
    * Outgoing result for each Beam.
    *
    * @param description String with result
    */
  final case class ActionPerformed(description: String)

  def props(ptc: PhaseTransitionCoil, ts: TargetingScanner): Props = Props(new Platform(ptc, ts))
}

/**
  * Beams subjects to locations. For each Location create a new Platform.
  *
  * @param ptc PhaseTransitionCoil implementation for transforming Matter to Energy for the specific Location
  * @param ts  TargetingScanner implementation for getting the correct Target for the specific Location
  */
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

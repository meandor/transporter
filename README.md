# Transporter

[![Build Status](https://travis-ci.org/meandor/transporter.svg?branch=master)](https://travis-ci.org/meandor/transporter)

A _Scala-Application_ that transports matter to locations using [Akka](https://akka.io/)
and [Prometheus](https://prometheus.io) metrics.

> "Transporting really is the safest way to travel."
>
> -- Geordi La Forge, 2369 ("Realm of Fear"), Star Trek

In other words it exports data to other locations. It can be used to export data to APIs. 
It accepts incoming data via REST.

## Configuring

To change configs like the port the HTTP server is listening to and similar,
change the `application.conf` file.

## Incoming Data
Add interfaces for dealing with incoming data at `de.transporter.console.TransporterConsole`
by registering [Akka-HTTP](https://doc.akka.io/docs/akka-http/current/scala/http/) routes.

## Outgoing Data
Register an Akka-Actor with the system for each location you want to transport to.
For that just add new `de.teleport.platform.Platform`s at `de.transporter.Transporter`.

Each Platform consists of a `PhaseTransitionCoil` and a `TargetingScanner`.
The `PhaseTransitionCoil` is responsible for transforming matter to energy or in other
words transform incoming data (e.g. engity) into data that can be exported (e.g. DTO).

The `TargetingScanner` is responsible to lock onto the target depending on the given location.
In other words find the proper API abstraction to export to (adapter), which is called `Target`.

To add an Export you have to add a new `Platform` actor with an Implementation of a
`PhaseTransitionCoil` and a `TargetingScanner` which define how to export incoming data.

In `de.transporter.Transporter`:
```scala
...
implicit val system: ActorSystem = ActorSystem("Transporter")
implicit val materializer: ActorMaterializer = ActorMaterializer()
implicit val executionContext: ExecutionContext = system.dispatcher

system.actorOf(Platform.props(SpecificPlatformPTC, SpecificPlatformTS), "SpecificPlatformName")
...
``` 

where `SpecificPlatformTS` and `SpecificPlatformPTC` tell the Platform on how to teleport.
```scala
object SpecificPlatformTS extends TargetingScanner {  
...
}

object SpecificPlatformPTC extends PhaseTransitionCoil {  
...
}
```

## Metrics
Prometheus metrics are exported to the HTTP server via the `/metrics` endpoint.

## Links
* http://memory-alpha.wikia.com/wiki/Transporter
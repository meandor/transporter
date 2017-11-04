# Transporter

[![Build Status](https://travis-ci.org/meandor/transporter.svg?branch=master)](https://travis-ci.org/meandor/transporter)

A _Scala-Library_ that transports matter to locations using [Akka](https://akka.io/)
and [Prometheus](https://prometheus.io) metrics.

> "Transporting really is the safest way to travel."
>
> -- Geordi La Forge, 2369 ("Realm of Fear"), Star Trek

In other words it exports data to other locations. It can be used to export data to APIs. 

![Transporter Image](http://www.startrek.com/uploads/assets/articles/transporter-1.jpg)

## Testing
Execute the tests with gradle:
```bash
./bin/go check
```

## Building
To build with gradle:
```bash
./bin/go clean build
```
This will create a jar file in `./build/libs` without the dependencies.

## Releasing and Publishing
```bash
./bin/go release
```
Will release a newer version with semantic versioning 

```bash
./bin/go publish
```

will publish the artifact to maven central.

## Outgoing Data
Register an Akka-Actor with the system for each location you want to transport to.
For that just add new `com.github.meandor.transporter.platform.Platform`s.

Each Platform consists of a `PhaseTransitionCoil` and a `TargetingScanner`.
The `PhaseTransitionCoil` is responsible for transforming matter to energy or in other
words transform incoming data (e.g. entity) into data that can be exported (e.g. DTO).

The `TargetingScanner` is responsible to lock onto the target depending on the given location.
In other words find the proper API abstraction to export to (adapter), which is called `Target`.

To add an Export you have to add a new `Platform` actor with an implementation of
`PhaseTransitionCoil` and `TargetingScanner` which define how to export incoming data.

```scala
...
system.actorOf(Platform.props(SpecificPlatformPTC, SpecificPlatformTS), "SpecificPlatformName")
...
``` 

where `SpecificPlatformTS` and `SpecificPlatformPTC` tell the Platform how to beam Matter and `system` is an Akka Actor System.
```scala
object SpecificPlatformTS extends TargetingScanner {  
...
}

object SpecificPlatformPTC extends PhaseTransitionCoil {  
...
}
```

## Example
https://github.com/meandor/voyage-transporter

## Metrics
Prometheus metrics are registered and available.

## Links
* http://memory-alpha.wikia.com/wiki/Transporter

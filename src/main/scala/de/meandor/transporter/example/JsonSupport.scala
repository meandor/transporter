package de.meandor.transporter.example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import de.meandor.transporter.platform.Platform.ActionPerformed
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport {

  import DefaultJsonProtocol._

  implicit val exampleMatterJsonFormat: RootJsonFormat[ExampleMatter] = jsonFormat2(ExampleMatter)

  implicit val exampleLocationJsonFormat: RootJsonFormat[ExampleLocation] = jsonFormat1(ExampleLocation)

  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)

  implicit val exampleRequestJsonFormat: RootJsonFormat[ExampleRequest] = jsonFormat2(ExampleRequest)
}

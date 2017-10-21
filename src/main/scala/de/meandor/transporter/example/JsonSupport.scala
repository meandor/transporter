package de.meandor.transporter.example

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import de.meandor.transporter.platform.Platform.ActionPerformed
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {

  import DefaultJsonProtocol._

  implicit val exampleMatterJsonFormat = jsonFormat2(ExampleMatter)

  implicit val exampleLocationJsonFormat = jsonFormat1(ExampleLocation)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)

  implicit val exampleRequestJsonFormat = jsonFormat2(ExampleRequest)
}

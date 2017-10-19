package de.transporter

import io.prometheus.client.hotspot.DefaultExports

object Transporter extends App {
  DefaultExports.initialize()
}

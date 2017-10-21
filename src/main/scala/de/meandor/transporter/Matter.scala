package de.meandor.transporter

/**
  * Matter that can be transported.
  */
trait Matter {
  def id: String

  def owner: String
}

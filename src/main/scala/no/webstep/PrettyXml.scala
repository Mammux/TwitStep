package no.webstep

import scala.xml.Elem

object PrettyXml {
  val pp = new scala.xml.PrettyPrinter(80,2)
}

trait PrettyXml {
  case class Formatted(xml:Elem) { 
    def pretty = PrettyXml.pp.format(xml) 
  }
  implicit def toFormatted(xml:Elem) = Formatted(xml)
}
package uk.org.nabbd

/**
 * Find information on a given police force if provided with a latitude and longitude, using the Police API.
 *
 * @author Inigo Surguy
 * @created 26/03/2011 15:17
 */

object PoliceFinder {

  def lookup(lat: String, long: String): Force = {
    Force("Metropolitan","inigo.surguy@gmail.com", "0123 4567890")
  }

}

case class Force(name: String, email: String, phone: String)
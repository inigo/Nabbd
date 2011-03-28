package uk.org.nabbd

import java.util.Properties

import xml.XML
import org.slf4j.LoggerFactory
import us.monoid.web.Resty

/**
 * Find information on a given police force if provided with a latitude and longitude, using the Police API.
 *
 * @author Inigo Surguy
 * @created 26/03/2011 15:17
 */
object PoliceFinder {
  val log = LoggerFactory.getLogger(PoliceFinder.getClass);

  def lookup(lat: String, long: String): Force = {

    val props = new Properties()
    props.load(this.getClass.getResourceAsStream("/nabbd.properties"))
    val forceDetails = XML.load(PoliceFinder.getClass.getResourceAsStream("/policeLookup.xml"))
    val policeApi = props.getProperty("police.api")
    val policeUsername = props.getProperty("police.username")
    val policePassword = props.getProperty("police.password")

    val r = new Resty()
    val url = "http://" + policeApi + lat + "," + long
    r.authenticate(url, policeUsername, policePassword.toCharArray)
    val json = r.json(url)
    val forceName = r.json(url).get("force")

    val policeXml = ((forceDetails \\ "force").filter(_.attribute("id").get.head.text == forceName)).head

    val name = policeXml.attribute("name").get.head.text
    val phone = (policeXml \ "phone").text
    val email = (policeXml \ "email").text

    log.info("Name: " + name + " Phone " + phone + " Email " + email)

    Force(name, email, phone)
  }

}

case class Force(name: String, email: String, phone: String)
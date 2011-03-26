package uk.org.nabbd

import java.util.Properties
import model.BurglaryReport
import net.liftweb.util.JSONParser._

import xml.{XML, Node}
import org.slf4j.LoggerFactory
import com.gargoylesoftware.htmlunit.{UnexpectedPage, DefaultCredentialsProvider, WebClient}

/**
 * Find information on a given police force if provided with a latitude and longitude, using the Police API.
 *
 * @author Inigo Surguy
 * @created 26/03/2011 15:17
 */

object PoliceFinder {


  def lookup(lat: String, long: String): Force = {

    val props = new Properties()
    props.load(this.getClass.getResourceAsStream("/nabbd.properties"))
    val forceDetails = XML.load(PoliceFinder.getClass.getResourceAsStream("/policeLookup.xml"))
    val policeApi = props.getProperty("police.api")
    val policeUsername = props.getProperty("police.username")
    val policePassword = props.getProperty("police.password")
    val log = LoggerFactory.getLogger(PoliceFinder.getClass);

    val webClient = new WebClient()

    val provider = new DefaultCredentialsProvider()
    provider.addCredentials(policeUsername, policePassword)
    webClient.setCredentialsProvider(provider)
    val url = "http://" + policeApi + lat + "," + long
    val response = webClient.getPage(url).asInstanceOf[UnexpectedPage]

    webClient.closeAllWindows

    val text = response.getWebResponse.getContentAsString

    // HORRIBLE but I can't figure out how to do JSON parsing.
    val forcePos = text.indexOf("force")
    val colon = text.indexOf(":", forcePos)
    val quote = text.indexOf("\"", colon)
    val nextQuote = text.indexOf("\"", quote + 1)

    val forceName = text.substring(quote+1, nextQuote);

    val policeXml = ((forceDetails \\ "force").filter(_.attribute("id").get.head.text == forceName)).head

    val name = policeXml.attribute("name").get.head.text
    val phone = (policeXml \ "phone").text
    val email = (policeXml \ "email").text

    log.info("Name: " + name + " Phone " + phone + " Email " + email)

    Force(name, email, phone)
  }

}

case class Force(name: String, email: String, phone: String)
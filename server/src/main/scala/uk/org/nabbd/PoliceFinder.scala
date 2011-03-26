package uk.org.nabbd

import java.util.Properties
import net.liftweb.util.JSONParser._

import xml.{XML, Node}
import com.gargoylesoftware.htmlunit.WebClient
import org.apache.http.impl.client.BasicCredentialsProvider

/**
 * Find information on a given police force if provided with a latitude and longitude, using the Police API.
 *
 * @author Inigo Surguy
 * @created 26/03/2011 15:17
 */

object PoliceFinder {

  val props = new Properties()
  props.load(this.getClass.getResourceAsStream("/nabbd.properties"))
  val forceDetails = XML.load(PoliceFinder.getClass.getResourceAsStream("policeLookup.xml"))
  val policeApi = props.getProperty("police.api")
  val policeUsername = props.getProperty("police.username")
  val policePassword = props.getProperty("police.password")


  def lookup(lat: String, long: String): Force = {


    val url = new java.net.URL("http://" + policeUsername + ":" + policePassword + "@" + policeApi + lat + "," + long)
    val response = url.openConnection().getContent().toString

    val json = parse(response)

    log.debug(response)



    Force("Metropolitan","inigo.surguy@gmail.com", "0123 4567890")
  }

//  private def getPage(url: String): String = {
//    val webClient = new WebClient()
//    val provider = new BasicCredentialsProvider()
//    provider.s
//    webClient.setCredentialsProvider()
//    webClient.setJavaScriptEnabled(false)
//    webClient.getPage(url).asInstanceOf[String]
//  }
}

case class Force(name: String, email: String, phone: String)
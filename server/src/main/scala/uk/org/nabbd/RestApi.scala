package uk.org.nabbd

import model.BurglaryReport
import net.liftweb.http.S
import net.liftweb.http.rest.RestHelper
import net.liftweb.util.Mailer
import xml.{Text, XML}

/**
 * Provides a REST API for generating new data.
 *
 * @author Inigo Surguy
 * @created 26/03/2011 13:35
 */

object RestApi extends RestHelper {

  /** Determine which requests we will respond to - this is called via the dispatch table set up in Boot. */
  serve {
    case "api" :: "BurglaryReport" :: _ Post _ =>
      (for {report <- S.param("report") ?~ "Param 'report' is missing" }
              yield addReport(report))
    case "api" :: "PolicePhone" :: _ Get _ =>
      (for {lat <- S.param("lat") ?~ "Param 'report' is missing";
            long <- S.param("long") ?~ "Param 'long' is missing" }
              yield addReport(lat))
  }

  def addReport(text: String) = {
    try {
      val xml = XML.loadString(text)
      val report: BurglaryReport = BurglaryReport.parseReport(xml)

      val force = PoliceFinder.lookup(report.latitude, report.longitude)
      sendEmail(force.email, report)

      <success/>
    } catch {
      case e: Exception => <failed>{ e.getMessage }</failed>
    }
  }

  def lookupPhoneNumber(lat: String, long: String) = {
    val phone = PoliceFinder.lookup(lat, long).phone
    Text(phone)
  }

  def sendEmail(to: String, report: BurglaryReport) = {
    // Sending email here...
    val subject = "Burglary report - crime number "+report.reportGuid
    val url = "http://nabbd.org.uk/burglaryReports/view?id="+report.reportGuid
    val body = """
        A burglary has been reported!

        Go to %s to view the details.
        """.format(url)
    Mailer.sendMail(Mailer.From("nabbd@nabbd.org.uk"), Mailer.Subject(subject), Mailer.To(to), Mailer.PlainMailBodyType(body))
  }

}
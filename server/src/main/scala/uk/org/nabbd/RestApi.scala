package uk.org.nabbd

import model.BurglaryReport
import net.liftweb.http.S
import xml.XML
import net.liftweb.http.rest.RestHelper
import net.liftweb.util.Mailer

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

  def sendEmail(to: String, report: BurglaryReport) = {
    // Sending email here...
    val subject = "Burglary report - crime number "+report.reportGuid
    val url = "http://nabbd.org.uk/reports/"+report.reportGuid
    val body = """
        A burglary has been reported!

        Go to %s to view the details.
        """.format(url)
    Mailer.sendMail(Mailer.From("nabbd@nabbd.org.uk"), Mailer.Subject(subject), Mailer.To(to), Mailer.PlainMailBodyType(body))
  }

}
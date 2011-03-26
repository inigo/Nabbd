package uk.org.nabbd

import model.BurglaryReport
import net.liftweb.http.S
import xml.XML
import net.liftweb.http.rest.RestHelper

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
      BurglaryReport.parseReport(xml)
      <success/>
    } catch {
      case e: Exception => <failed>{ e.getMessage }</failed>
    }
  }

}
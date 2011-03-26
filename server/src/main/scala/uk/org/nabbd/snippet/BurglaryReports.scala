package uk.org.nabbd.snippet

import net.liftweb.util.CssBind
import uk.org.nabbd.model.BurglaryReport
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.util.BindHelpers._
import net.liftweb.http.SHtml
import java.util.Date
import _root_.net.liftweb.http.SHtml._


/**
 * List all of the burglaries that have been reported by you.
 *
 * @author Inigo Surguy
 * @created 26/03/2011 12:50
 */

class BurglaryReports {

  def list(): CssBind =
    ".line *" #> BurglaryReport.findAll.map(
        w =>
         ".user *" #> w.userGuid
       & ".latitude *" #> w.latitude
       & ".longitude *" #> w.longitude
       & ".reportedAt *" #> w.reportDate
       & ".delete *" #> ajaxButton("Delete", {() =>
          w.delete_!
          Run("window.location.reload()")
        })
    )

  def createDummy() : CssBind =
      ".button" #> ajaxButton("Create dummy data", {() =>
          BurglaryReport.create.latitude("12.2123").longitude("5.123123").createdAt(new Date()).save()
          Run("window.location.reload()")
      })

}
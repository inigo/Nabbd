package uk.org.nabbd.snippet

import net.liftweb.util.CssBind
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.util.BindHelpers._
import net.liftweb.http.SHtml
import java.util.Date
import _root_.net.liftweb.http.SHtml._
import uk.org.nabbd.model.{Item, BurglaryReport}


/**
 *
 *
 * @author Daniel Rendall
 */

class CommissionerReports {
  def list(): CssBind = {

    ".line *" #> BurglaryReport.findAll.map(
        w =>
         ".user *" #> w.userGuid
           & ".createdAt" #> w.createdAt
       & ".reportedAt *" #> w.reportDate
       & ".totalValue *" #> ("&" + Item.findAll.withFilter(_.reportGuid == w.reportGuid).map(_.price.toString.toInt).sum.toString)
      )
    }

  def createDummy() : CssBind =
      ".button" #> ajaxButton("Create dummy data", {() =>
          BurglaryReport.create.userGuid("12345678876543211234567887654321").latitude("12.2123").longitude("5.123123").createdAt(new Date()).save()
          Run("window.location.reload()")
      })

}
package uk.org.nabbd.snippet

import net.liftweb.util.CssBind
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.util.BindHelpers._
import net.liftweb.http.SHtml
import java.util.Date
import _root_.net.liftweb.http.SHtml._
import uk.org.nabbd.model.{Item, BurglaryReport}
import net.liftweb.mapper.By


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
           & ".createdAt" #> w.createdAt
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
          BurglaryReport.create.userGuid("12345678876543211234567887654321").latitude("12.2123").longitude("5.123123").createdAt(new Date()).save()
          Run("window.location.reload()")
      })

  def viewDetails(): CssBind =
      ".reportedAt *" #> BurglaryReport.findAll.last.reportDate

  def viewItems(): CssBind = {
    val reportId = BurglaryReport.findAll.last.reportGuid
    ".line *" #> Item.find(By(Item.reportGuid, reportId) ).map(
        w =>
         ".name *" #> w.name
       & ".category *" #> w.category
       & ".serial *" #> w.serial
       & ".smartwater *" #> w.smartwater
       & ".barcode *" #> w.barcode
       & ".price *" #> w.price
       & ".isStolen *" #> w.isStolen
       & ".delete *" #> ajaxButton("Delete", {() =>
          w.delete_!
          Run("window.location.reload()") // Runs arbitrary JavaScript
        })
    )
  }

  def viewLocation(): CssBind =
        ".lat *" #> BurglaryReport.findAll.last.latitude
//        & ".long *" #> BurglaryReport.findAll.last.longitude

}
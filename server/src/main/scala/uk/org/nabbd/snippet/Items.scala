package uk.org.nabbd.snippet

import net.liftweb.util.CssBind
import uk.org.nabbd.model.Item
import net.liftweb.util.BindHelpers._
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds.{Run, SetHtml}
import _root_.net.liftweb.http.SHtml._
import xml.{Group, Text}
import java.util.Date

/**
 * Display the items that you own.
 *
 * @author Inigo Surguy
 * @created 26/03/2011 12:50
 */

class Items {

  def list(): CssBind =
    ".line *" #> Item.findAll.map(
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

  def createDummy() : CssBind =
      ".button" #> ajaxButton("Create dummy data", {() =>
          Item.create.name("some name").category("some category").barcode("Some barcode").price("£200").serial("some serial").createdAt(new Date()).save()
          Run("window.location.reload()") // Runs arbitrary JavaScript
      })

}
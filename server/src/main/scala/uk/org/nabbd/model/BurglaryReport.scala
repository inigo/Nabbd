package uk.org.nabbd.model

import net.liftweb.mapper._
import xml.Node
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 26/03/2011 11:47
 */

class BurglaryReport extends LongKeyedMapper[BurglaryReport] {
  def getSingleton = BurglaryReport

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object name extends MappedString(this, 50)
  object createdAt extends MappedDateTime(this)

//  override def toXml = <item id={id}>{word}</item>
}

object BurglaryReport extends BurglaryReport with LongKeyedMetaMapper[BurglaryReport] with CRUDify[Long, BurglaryReport] {

  val log = LoggerFactory.getLogger(BurglaryReport.getClass);
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")

  def parseReport(aReport : Node) = {
    val now = new Date
    aReport match {
      case <nabbd>{contents @ _* }</nabbd> => {
        val burglary = contents \\ "burglary"
        log.debug(burglary.toString)
        val userGuid = (burglary \ "@userGuid").toString
        val reportGuid = (burglary \ "@reportGuid").toString
        val reportDate = dateFormat.parse((burglary \ "@reportDate").toString)
        val details = (burglary \ "details").head

        val latitude = getLocationValue(details, "latitude")
        val longitude = getLocationValue(details, "longitude")
        val accuracy = getLocationValue(details, "accuracy")


        val items = (burglary \ "items").head

        for (itemEl <- (items \ "item")) {
          val name = (itemEl \ "name").text
          val serial = (itemEl \ "serial").text
          val smartwaterUse = (itemEl \ "smartwater" \ "@use").text
          val smartwater = (itemEl \ "smartwater").text
          val barcodeType = (itemEl \ "barcode" \ "@type").text
          val barcode = (itemEl \ "barcode").text
          val category = (itemEl \ "category").text
          val price = (itemEl \ "price").text

          log.debug("Name " + name + " Serial " + serial + " SmartwaterUse" + smartwaterUse + " Smartwater " + smartwater + " BarcodeType " + barcodeType + " Barcode " + barcode + " Price " + price)

          val item = Item.create.name(name).category(category).serial(serial).smartwater(smartwater).barcode(barcode).price(price).isStolen(true).createdAt(now).save()
        }

      }
    }
  }

  def getLocationValue(details : Node, which : String) : String = {
      (details \ "location").head match {
        case <location>{contents @ _*}</location> => {
           (contents \\ which).text
        }
        case _ => { "" }
      }
  }

}

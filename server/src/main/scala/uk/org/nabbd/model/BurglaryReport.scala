package uk.org.nabbd.model

import net.liftweb.mapper._
import xml.Node
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @todo Add some documentation!Fixed up the fields in the BurglaFixed up the fields in the BurglaryReport class and resolved differencesryReport class and resolved differences
 *
 * @author Inigo Surguy
 * @created 26/03/2011 11:47
 */

class BurglaryReport extends LongKeyedMapper[BurglaryReport] with IdPK {
  def getSingleton = BurglaryReport

  object userGuid extends MappedString(this, 50)
  object reportGuid extends MappedString(this, 50)
  object reportDate extends MappedDateTime(this)
  object latitude extends MappedString(this, 50)
  object longitude extends MappedString(this, 50)
  object accuracy extends MappedString(this, 50)
  object createdAt extends MappedDateTime(this)

  def items = Item.findAll(By(Item.burglaryReport, this.id))

//  override def toXml = <item id={id}>{word}</item>
}

object BurglaryReport extends BurglaryReport with LongKeyedMetaMapper[BurglaryReport] with CRUDify[Long, BurglaryReport] {

  val log = LoggerFactory.getLogger(BurglaryReport.getClass);
  val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")

  def parseReport(aReport : Node) : BurglaryReport = {
    val now = new Date
    aReport match {
      case <nabbd>{contents @ _* }</nabbd> => {
        val burglaryEl = contents \\ "burglary"
        val userGuid = (burglaryEl \ "@userGuid").toString
        val reportGuid = (burglaryEl \ "@reportGuid").toString
        val reportDate = dateFormat.parse((burglaryEl \ "@reportDate").toString)
        val details = (burglaryEl \ "details").head

        val latitude = getLocationValue(details, "latitude")
        val longitude = getLocationValue(details, "longitude")
        val accuracy = getLocationValue(details, "accuracy")

        val burglary = BurglaryReport.create
          .userGuid(userGuid)
          .reportGuid(reportGuid)
          .reportDate(reportDate)
          .latitude(latitude)
          .longitude(longitude)
          .accuracy(accuracy)
          .createdAt(now)

        burglary.save()

        val items = (burglaryEl \ "items").head

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

          val item = Item.create
            .name(name)
            .reportGuid(reportGuid)
            .category(category)
            .serial(serial)
            .smartwater(smartwater)
            .barcode(barcode)
            .price(price)
            .isStolen(true)
            .createdAt(now)
            .save()

        }
        burglary
      }
      case _ => {
        null
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

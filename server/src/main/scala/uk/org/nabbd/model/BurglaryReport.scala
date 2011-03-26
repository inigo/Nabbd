package uk.org.nabbd.model

import net.liftweb.mapper._

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

}

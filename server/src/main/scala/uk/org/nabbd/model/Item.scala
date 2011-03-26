package uk.org.nabbd.model

import net.liftweb.mapper._

/**
 * An item held by a user
 *
 * @author Inigo Surguy
 * @created 26/03/2011 11:47
 */
class Item extends LongKeyedMapper[Item] with IdPK {
  def getSingleton = Item

  object name extends MappedString(this, 50)
  object reportGuid extends MappedString(this, 50)
  object category extends MappedString(this, 50)
  object serial extends MappedString(this, 50)
  object smartwater extends MappedString(this, 50)
  object barcode extends MappedString(this, 50)
  object price extends MappedString(this, 50)

  object isStolen extends MappedBoolean(this)

  object createdAt extends MappedDateTime(this)

  object burglaryReport extends MappedLongForeignKey(this, BurglaryReport)
//  override def toXml = <item id={id}>{word}</item>
}

object Item extends Item with LongKeyedMetaMapper[Item] with CRUDify[Long, Item] {

}

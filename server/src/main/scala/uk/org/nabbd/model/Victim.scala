package uk.org.nabbd.model

import net.liftweb.mapper._

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 26/03/2011 11:46
 */

class Victim extends LongKeyedMapper[Victim] {
  def getSingleton = Victim

  def primaryKeyField = id
  object id extends MappedLongIndex(this)
  object name extends MappedString(this, 50)
  object createdAt extends MappedDateTime(this)

//  override def toXml = <item id={id}>{word}</item>
}

object Victim extends Victim with LongKeyedMetaMapper[Victim] with CRUDify[Long, Victim] {

}

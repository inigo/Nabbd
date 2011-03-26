package uk.org.nabbd

import net.liftweb.util.Props
import net.liftweb.mapper.{DefaultConnectionIdentifier, DB, StandardDBVendor}
import bootstrap.liftweb.Boot
import org.specs.Specification

/**
 * Mix-in for tests ensuring that the database is set up before the test.
 *
 * @author Inigo Surguy
 * @created 27/02/2011 10:08
 */

trait DatabaseSetup extends Specification {

  doBeforeSpec { setupDatabase() }

  def setupDatabase() = {
    println("Setting up database")
    val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
       Props.get("db.url") openOr
       "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
       Props.get("db.user"), Props.get("db.password"))
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    new Boot().createDatabaseTables()
  }

}
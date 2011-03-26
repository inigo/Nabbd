package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, StandardDBVendor}
import _root_.java.sql.{Connection, DriverManager}
import uk.org.nabbd.model.{Victim, BurglaryReport, Item}
import uk.org.nabbd.RestApi

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    DefaultConnectionIdentifier.jndiName = Props.get("db.jndiName") openOr "jdbc/nabbd"
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr 
			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }


    // Use the RestApi object for REST dispatch - this is stateful, but the "S" object is not populated
    // when using the statelessDispatchTable, so the way we're retrieving parameters doesn't work
    LiftRules.dispatch.append(RestApi) // stateful -- associated with a servlet container session

    // where to search for snippets
    LiftRules.addToPackages("uk.org.nabbd")

    // Format documented on the Wiki at http://www.assembla.com/wiki/show/liftweb/SiteMap
    def sitemap() = SiteMap(
      Menu(S ? "Welcome") / "index" 
      , Menu(S ? "Help") / "static" / "help"
     , Menu(S ? "My Stuff") / "items" / "list"
     , Menu(S ? "My Reports") / "burglaryReports" / "list"
    )

    LiftRules.setSiteMapFunc(() => sitemap())

    /* Show the spinny image when an Ajax call starts */
    LiftRules.ajaxStart = Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /* Make the spinny image go away when it ends */
    LiftRules.ajaxEnd = Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    S.addAround(DB.buildLoanWrapper)

    createDatabaseTables()
  }

  def createDatabaseTables() = {
    Schemifier.schemify(true, Schemifier.infoF _, Item)
    Schemifier.schemify(true, Schemifier.infoF _, BurglaryReport)
    Schemifier.schemify(true, Schemifier.infoF _, Victim)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}

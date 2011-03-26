package uk.org.nabbd

import model.BurglaryReport
import org.specs.SpecificationWithJUnit
import java.util.Properties
import bootstrap.liftweb.Boot

/**
 * @todo Add some documentation!
 *
 * @author Inigo Surguy
 * @created 26/03/2011 15:34
 */

class MailSendingTest extends SpecificationWithJUnit {

  "receiving a burglary report" should {
    "send a mail" in {
      val myprops = new Properties()
      myprops.load(this.getClass.getResourceAsStream("/nabbd.properties"))
      new Boot().configMailer(myprops.getProperty("mail.server"), myprops.getProperty("mail.username"), myprops.getProperty("mail.password"))

      val report = new BurglaryReport().reportGuid("1234567890")
      RestApi.sendEmail("inigo.surguy@gmail.com", report)
    }
  }

}

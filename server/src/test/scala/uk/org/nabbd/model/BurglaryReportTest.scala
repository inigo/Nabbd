package uk.org.nabbd.model;

import org.specs.SpecificationWithJUnit
import org.specs._
import org.specs.matcher._
import uk.org.nabbd.DatabaseSetup

/**
 * Sample Specs test.
 */
class BurglaryReportTest extends SpecificationWithJUnit with DatabaseSetup {

  "A BurglaryReport" should {
    
    "parse a burglary report correctly" in {
      val report = <nabbd version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="nabbd.xsd">
    <burglary userGuid="123456789012345678901234567890ab" reportGuid="123456789012345678901234567890ab" reportDate="2011-03-26T01:03:34Z">
        <details>
          <location>
              <latitude>45.6</latitude>
              <longitude>34.9</longitude>
              <accuracy>3</accuracy>
            </location>
        </details>
        <items>
            <item itemGuid="123456789012345678901234567890ab">
            	<name>Daniel's bicycle</name>
                <serial>02834982542</serial>
                <smartwater use="true">1234</smartwater>
                <barcode type="upc">1234</barcode>
                <category>A category</category>
                <price>100</price>
                <photos>
                    <photo dateTaken="2011-03-26T01:03:30Z"></photo>
                </photos>
            </item>
            <item itemGuid="123456789012345678901234567890ac">
            	<name>An expensive laptop</name>
                <serial>02834982542</serial>
                <smartwater use="false"></smartwater>
                <barcode type="upc"></barcode>
                <category>Expensive things</category>
                <price>900</price>
                <photos>
                    <photo dateTaken="2011-02-23T01:03:40Z"></photo>
                </photos>
            </item>
        </items>
    </burglary>
</nabbd>;

      BurglaryReport.parseReport(report);
    }
  }

}

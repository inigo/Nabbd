package uk.org.nabbd

import org.specs.SpecificationWithJUnit
import org.slf4j.LoggerFactory

/**
 *
 *
 * @author Daniel Rendall
 */
class PoliceFinderTest extends SpecificationWithJUnit {
  "Looking up police forces" should {
    "find the Metropolitan Police for co-ordinates in London" in {
      PoliceFinder.lookup("51.500617", "-0.124629").name must beEqualTo("Metropolitan Police")
    }
    "find the Met's phone number for co-ordinates in London" in {
      PoliceFinder.lookup("51.500617", "-0.124629").phone must beEqualTo("0300 123 1212")
    }
    "find Thames Valley Police for co-ordinates in Oxford" in {
      PoliceFinder.lookup("51.751944", "-1.25777851").name must beEqualTo("Thames Valley Police")
    }
  }
}
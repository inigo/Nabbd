package uk.org.nabbd

import org.specs.SpecificationWithJUnit
import org.specs._
import org.specs.matcher._

/**
 * Sample Specs test.
 */
class SampleTest extends SpecificationWithJUnit {

  "A List" should {
    "have a size method returning the number of elements in the list" in {
      List(1, 2, 3).size must_== 3
    }
  }

}

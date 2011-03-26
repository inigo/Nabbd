package uk.org.nabbd

import org.specs.SpecificationWithJUnit

/**
 *
 *
 * @author Daniel Rendall
 */

class PoliceFinderTest extends SpecificationWithJUnit {

  "A PoliceFinder" should {

    "find a police station" in {
      val pf = PoliceFinder.lookup("51.500617", "-0.124629")
      }
    }
  }
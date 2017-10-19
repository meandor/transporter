import org.scalatest.{FeatureSpec, Matchers}

class LibrarySuite extends FeatureSpec with Matchers {
  feature("failing test") {
    scenario("failing") {
      false shouldBe true
    }
  }
}

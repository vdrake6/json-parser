package nullpointer.json.testing

import nullpointer.json.testing.TryMatchers.TryFailWithTypeMatcher

import scala.util.{Failure, Success}

class TryFailWithTypeMatcherSpec extends CommonSpec {
  describe("A TryFailWithMatcher") {
    it("must match when failed with exception of expected type") {
      val matcher = new TryFailWithTypeMatcher[IllegalStateException]
      val result = matcher(Failure(new IllegalStateException("Some exception message")))
      result.matches mustBe true
    }

    it("must not match when try is success") {
      val matcher = new TryFailWithTypeMatcher[IllegalStateException]
      val result = matcher(Success("abc"))
      result.matches mustBe false
    }

    it("must not match when failed with exception of type other than expected") {
      val matcher = new TryFailWithTypeMatcher[IllegalStateException]
      val result = matcher(Failure(new IllegalAccessError("Some exception message")))
      result.matches mustBe false
    }
  }
}

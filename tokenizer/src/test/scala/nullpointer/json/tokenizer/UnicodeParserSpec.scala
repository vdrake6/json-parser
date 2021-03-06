package nullpointer.json.tokenizer

import nullpointer.json.testing.{CommonSpec, TryMatchers}
import nullpointer.json.tokenizer.UnicodeParser.UnicodeFormatException
import org.scalatest.prop.TableDrivenPropertyChecks._

class UnicodeParserSpec extends CommonSpec with TryMatchers {
  describe("An UnicodeParser") {
    it("must return correct character when have valid code string") {
      val validCodeStrings = Table(
        ("validCodeString", "expectedChar"),
        ("abcd", 43981.toChar),
        ("dcba", 56506.toChar),
        ("0123", 291.toChar),
        ("3210", 12816.toChar),
        ("AFF0", 45040.toChar),
        ("dF1B", 57115.toChar),
        ("0000", 0.toChar),
        ("ffff", 65535.toChar),
        ("FFFF", 65535.toChar)
      )
      forAll(validCodeStrings) { (validCodeString, expectedChar) =>
        val result = UnicodeParser.parse(validCodeString)
        result must succeedWith(expectedChar)
      }
    }

    it("must fail with UnicodeFormatException when code string length is not 4") {
      val invalidCodeStrings = Table(
        "invalidCodeString",
        "",
        "a",
        "ab",
        "abc",
        "abcde"
      )
      forAll(invalidCodeStrings) { invalidCodeString =>
        val result = UnicodeParser.parse(invalidCodeString)
        result must failWith[UnicodeFormatException]
      }
    }

    it("must fail with UnicodeFormatException when code string contains non hexadecimal characters") {
      val invalidCodeStrings = Table(
        "invalidCodeString",
        "@b2a",
        "9ka1",
        "52tb",
        "abcg",
        "@!$#",
        "poiu"
      )
      forAll(invalidCodeStrings) { invalidCodeString =>
        val result = UnicodeParser.parse(invalidCodeString)
        result must failWith[UnicodeFormatException]
      }
    }
  }
}

package nullpointer.json.formats

import nullpointer.json.JsonValues._
import nullpointer.json.formats.JsonFormatExceptions.JsonDeserializationException
import nullpointer.json.testing.JsonFormatSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.util.Random

class ByteJsonFormatSpec extends JsonFormatSpec {
  import ByteJsonFormatSpec._

  describe("A ByteJsonFormat") {
    it("must serialize to JsonNumber with correct value") {
      // TODO: Generate DISTINCT numbers
      val serializeByteTestCases = Table(
        ("number", "expectedValue"),
        (1 to 100)
          .map(_ => nextByte)
          .map(n => n -> JsonNumber(n)):_*
      )
      forAll(serializeByteTestCases) { (number, expectedValue) =>
        val result = ByteJsonFormat.serialize(number)
        result must succeedWith[JsonValue](expectedValue)
      }
    }

    it("must deserialize JsonNumber to correct Byte") {
      val deserializeJsonNumberTestCases = Table(
        ("json", "expectedNumber"),
        (1 to 100)
          .map(_ => Random.nextDouble)
          .map(n => JsonNumber(n) -> n.toByte):_*
      )
      forAll(deserializeJsonNumberTestCases) { (json, expectedNumber) =>
        val result = ByteJsonFormat.deserialize(json)
        result must succeedWith(expectedNumber)
      }
    }

    it("must fail with JsonDeserializationException on deserialization when JSON is not JsonNumber") {
      val failingDeserializationTestCases = Table(
        "json",
        JsonNull,
        JsonBoolean(false),
        JsonBoolean(true),
        JsonString("abc"),
        JsonArray(JsonBoolean(true), JsonNull),
        JsonObject("a" -> JsonNull, "b" -> JsonBoolean(false))
      )
      forAll(failingDeserializationTestCases) { json =>
        val result = ByteJsonFormat.deserialize(json)
        result must failWith[JsonDeserializationException]
      }
    }
  }
}

private object ByteJsonFormatSpec {
  private lazy val nextByteUpperBound: Int = Byte.MaxValue + Byte.MinValue.toInt.abs

  def nextByte: Byte =
    (Random.nextInt(nextByteUpperBound) - Byte.MinValue.abs).toByte
}

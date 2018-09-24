package nullpointer.json

import nullpointer.json.testing.CommonSpec

import nullpointer.json.JsonValues._

class JsonSpec extends CommonSpec {
  describe("Json") {
    it("must parse null") {
      val result = Json.parse("null")
      result.isSuccess mustBe true
      result.get mustBe JsonNull
    }

    it("must parse boolean false") {
      val result = Json.parse("false")
      result.isSuccess mustBe true
      result.get mustBe JsonBoolean(false)
    }

    it("must parse boolean true") {
      val result = Json.parse("true")
      result.isSuccess mustBe true
      result.get mustBe JsonBoolean(true)
    }

    it("must parse zero") {
      val result = Json.parse("0")
      result.isSuccess mustBe true
      result.get mustBe JsonNumber(0)
    }

    it("must parse negative integer") {
      val result = Json.parse("-579")
      result.isSuccess mustBe true
      result.get mustBe JsonNumber(-579)
    }

    it("must parse positive integer") {
      val result = Json.parse("791")
      result.isSuccess mustBe true
      result.get mustBe JsonNumber(791)
    }

    it("must parse negative decimal") {
      val result = Json.parse("-105.69213")
      result.isSuccess mustBe true
      result.get mustBe JsonNumber(-105.69213)
    }

    it("must parse positive decimal") {
      val result = Json.parse("4056.791")
      result.isSuccess mustBe true
      result.get mustBe JsonNumber(4056.791)
    }

    it("must parse empty string") {
      val result = Json.parse("\"\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("")
    }

    it("must parse string") {
      val result = Json.parse("\"abcdefghij\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("abcdefghij")
    }

    it("must parse string with escaped quotation mark") {
      val result = Json.parse("\"abc\\\"def\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("abc\"def")
    }

    it("must parse string with escaped reverse solidus") {
      val result = Json.parse("\"abc\\\\def\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("abc\\def")
    }

    it("must parse string with escaped solidus") {
      val result = Json.parse("\"abc\\/def\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("abc/def")
    }

    it("must parse string with escaped backspace") {
      val result = Json.parse("\"abc\\bdef\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString(s"abc${8.toChar}def")
    }

    it("must parse string with escaped formfeed") {
      val result = Json.parse("\"abc\\fdef\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString(s"abc${12.toChar}def")
    }

    it("must parse string with escaped newline") {
      val result = Json.parse("\"abc\\ndef\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("abc\ndef")
    }

    it("must parse string with escaped carriage return") {
      val result = Json.parse("\"abc\\rdef\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("abc\rdef")
    }

    it("must parse string with escaped unicode character") {
      val result = Json.parse("\"abc\\u3a9fdef")
      result.isSuccess mustBe true
      result.get mustBe JsonString(s"abc${15007.toChar}def")
    }

    it("must parse string with escaped horizontal tab") {
      val result = Json.parse("\"abc\\tdef\"")
      result.isSuccess mustBe true
      result.get mustBe JsonString("abc\tdef")
    }

    it("must parse empty array") {
      val result = Json.parse("[]")
      result.isSuccess mustBe true
      result.get mustBe JsonArray(List.empty)
    }

    it("must parse array") {
      val resultTry = Json.parse("[null, true, -5.9871, \"abc\"]")
      resultTry.isSuccess mustBe true
      val result = resultTry.get
      result.isInstanceOf[JsonArray] mustBe true
      val resultArray = result.asInstanceOf[JsonArray]
      resultArray.elements must contain theSameElementsAs Seq(
        JsonNull,
        JsonBoolean(true),
        JsonNumber(-5.9871),
        JsonString("abc")
      )
    }

    it("must parse empty object") {
      val result = Json.parse("{}")
      result.isSuccess mustBe true
      result.get mustBe JsonObject()
    }

    it("must parse object") {
      val resultTry = Json.parse("{ \"a\": null, \"b\": true, \"c\": -5.9871, \"d\": \"abc\" }")
      resultTry.isSuccess mustBe true
      val result = resultTry.get
      result.isInstanceOf[JsonObject] mustBe true
      val resultObject = result.asInstanceOf[JsonObject]
      resultObject.elements must contain theSameElementsAs Map(
        "a" -> JsonNull,
        "b" -> JsonBoolean(true),
        "c" -> JsonNumber(-5.9871),
        "d" -> JsonString("abc")
      )
    }
  }
}
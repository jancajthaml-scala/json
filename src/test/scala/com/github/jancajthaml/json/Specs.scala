package com.github.jancajthaml.json

import collection.mutable.Stack
import org.scalatest._

class JSONSpecs extends FlatSpec with Matchers {

  "jsondumps" should "be json" in {
    val map = Map(
      "A" -> 1,
      "B" -> null,
      "C" -> true,
      "D" -> false,
      "E" -> "e"
    )
    val json = jsondumps(map)

    json should startWith ("{")
    json should endWith ("}")
  }

  it should "serialize with type awareness" in {
    val map = Map(
      "A" -> 1,
      "B" -> null,
      "C" -> true,
      "D" -> false,
      "E" -> "e"
    )
    val json = jsondumps(map)

    json should include ("\"A\":1")
    json should include ("\"B\":null")
    json should include ("\"C\":true")
    json should include ("\"D\":false")
    json should include ("\"E\":\"e\"")
  }

  //@todo TBD
  /*
  it should "serialize nested maps" in {
    val map = Map("A1" -> Map("B1" -> Map("C" -> "D"), "B2" -> "V1"), "A2" -> "V2")
    1 should === (1)
  }*/

  it should "serialize Int and Long correctly" in {
    var someInt: Int = 123
    val maxInt: Int = Integer.MAX_VALUE
    val someLong: Long = (Integer.MAX_VALUE.asInstanceOf[Long] + 100).asInstanceOf[Long]
    val maxLong: Long = Long.MaxValue

    val map = Map(
      "someInt" -> someInt,
      "maxInt" -> maxInt,
      "someLong" -> someLong,
      "maxLong" -> maxLong
    )
    val json = jsondumps(map)

    json should include ("\"someInt\":" + someInt)
    json should include ("\"maxInt\":" + maxInt)
    json should include ("\"someLong\":" + someLong)
    json should include ("\"maxLong\":" + maxLong)
  }

  "jsonloads" should "deserialize with type awareness" in {
    val map = jsonloads("""{"E":"e","A":1,"B":null,"C":true,"D":false}""")

    map.keys should have size (5)

    map.getOrElse("A", None) should === (1)
    map.getOrElse("B", None) should === (null)
    map.getOrElse("C", None) should === (true)
    map.getOrElse("D", None) should === (false)
    map.getOrElse("E", None) should === ("e")
  }

  it should "deserialize multiline/malformed verbose" in {
    var someInt: Int = 123
    val map = jsonloads(s"""
      {



                    "ID" : "SGML",
          "SortAs"          : "SGML"   ,
              "GlossTerm": "Standard Generalized Markup Language" ,
          "Acronym"     : "SGML",
          "Abbrev": "ISO 8879:1986",
          "GlossDef": $someInt , "GlossSee": "markup"
  

            }
    """)

    map.keys should have size (7)

    map.getOrElse("ID", None) should === ("SGML")
    map.getOrElse("SortAs", None) should === ("SGML")
    map.getOrElse("GlossTerm", None) should === ("Standard Generalized Markup Language")
    map.getOrElse("Abbrev", None) should === ("ISO 8879:1986")
    map.getOrElse("GlossDef", None) should === (someInt)
    map.getOrElse("GlossSee", None) should === ("markup")
  }

  it should "deserialize Int and Long correctly" in {
    var someInt: Int = 123
    val maxInt: Int = Integer.MAX_VALUE
    val someLong: Long = (Integer.MAX_VALUE.asInstanceOf[Long] + 100).asInstanceOf[Long]
    val maxLong: Long = Long.MaxValue

    val map = jsonloads(s"""{"someInt":$someInt,"maxInt":$maxInt,"someLong":$someLong,"maxLong":$maxLong}""")

    map.keys should have size (4)

    map.getOrElse("someInt", None) should === (someInt)
    map.getOrElse("someLong", None) should === (someLong)
    map.getOrElse("maxInt", None) should === (maxInt)
    map.getOrElse("maxLong", None) should === (maxLong)
  }

  "jsonloads + jsondumps" should "compress json" in {
    val map = jsonloads("""
      {



                    "ID" : "SGML",
          "SortAs"          : "SGML"   ,
              "GlossTerm": "Standard Generalized Markup Language" ,
          "Acronym"     : "SGML",
          "Abbrev": "ISO 8879:1986",
          "GlossDef": 123 , "GlossSee": "markup"
  

            }
    """)

    val json = jsondumps(map)

    val expectedString = """{"ID":"SGML","Acronym":"SGML","GlossDef":123,"SortAs":"SGML","GlossSee":"markup","Abbrev":"ISO 8879:1986","GlossTerm":"Standard Generalized Markup Language"}"""

    json should have length (expectedString.length())

  }

}
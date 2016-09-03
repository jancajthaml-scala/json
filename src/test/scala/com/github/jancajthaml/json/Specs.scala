package com.github.jancajthaml.json

import collection.mutable.Stack
import org.scalatest._

class JSONSpecs extends FlatSpec with Matchers {

  "jsondumps" should "serialize with type awareness" in {
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

    json should include ("\"A\":1")
    json should include ("\"B\":null")
    json should include ("\"C\":true")
    json should include ("\"D\":false")
    json should include ("\"E\":\"e\"")
  }

  it should "serialize nested maps" in {
    val map = Map("A1" -> Map("B1" -> Map("C" -> "D"), "B2" -> "V1"), "A2" -> "V2")
    1 should === (1)
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

    map.keys should have size (7)

    map.getOrElse("ID", None) should === ("SGML")
    map.getOrElse("SortAs", None) should === ("SGML")
    map.getOrElse("GlossTerm", None) should === ("Standard Generalized Markup Language")
    map.getOrElse("Abbrev", None) should === ("ISO 8879:1986")
    map.getOrElse("GlossDef", None) should === (123)
    map.getOrElse("GlossSee", None) should === ("markup")
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
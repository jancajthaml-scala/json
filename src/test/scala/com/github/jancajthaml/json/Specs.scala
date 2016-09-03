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

  "jsonloads" should "deserialize with type awareness" in {
    val map = jsonloads("""{"E":"e","A":1,"B":null,"C":true,"D":false}""")

    map.keys should have size (5)

    map.getOrElse("A", None) should === (1)
    map.getOrElse("B", None) should === (null)
    map.getOrElse("C", None) should === (true)
    map.getOrElse("D", None) should === (false)
    map.getOrElse("E", None) should === ("e")
  }

}
package com.github.jancajthaml.json

/**
  * Preallocated lookup variables for json dumps/loads
  *
  * @author jan.cajthaml
  */
private[json] object json {

  val nothing: Vector[Any] = Vector('"', ':', 'n', 'u', 'l', 'l', ',')
  val quotecomma: Vector[Any] = Vector('"', ',')
  val quotecolonquote: Vector[Any] = Vector('"', ':', '"')
  val quotecolon: Vector[Any] = Vector('"', ':')

  /*
    (t|f) => boolean (true|false)
    (0|1|2|3|4|5|6|7|8|9) => integral number (decimals unsuported)
    (") => string
    (n) => null
    (u) => undefined or unicode ... skip
    (e) => decimal number sci notation ... skip
  */
  val char2fn = Map(
    '"' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.drop(1).dropRight(1))),
    't' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> true)),
    'f' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> false)),
    'n' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> null)),
    '0' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> 0)),
    '1' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '2' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '3' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '4' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '5' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '6' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '7' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '8' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt)),
    '9' -> ((k:String, v:String, r:Map[String, Any]) => r + (k -> v.toInt))
  )

  val pass = ((k:String, v:String, r:Map[String, Any]) => r)
}

object jsondumps extends (Map[String, Any] => String) {
  
  import json.{nothing, quotecomma, quotecolonquote, quotecolon}

  def apply(value: Map[String, Any]): String = {
    //@todo string construction hogs this, should refactor to
    //recursion returning chunks of Array[Byte] in sudo parallel
    var vector: Vector[Any] = Vector[Any]()

    value.map(x => x._2 match {
      case d: String => vector = (vector ++ ('"' +: x._1.asInstanceOf[String]) ++ quotecolonquote ++ x._2.asInstanceOf[String] ++ quotecomma)
      case null => vector = (vector ++ ('"' +: x._1.asInstanceOf[String]) ++ nothing)
      case c => vector = (vector ++ ('"' +: x._1.asInstanceOf[String]) ++ quotecolon ++ (x._2.toString :+ ','))
    })

    '{' + vector.dropRight(1).mkString + '}'
  }
}

object jsonloads extends (String => Map[String, Any]) {

  import json.{char2fn, pass}

  def apply(value: String): Map[String, Any] = {
    //@todo check string contains nested json (more than one "{" or "}")
    //because we do not support nested json strucures @todo TBD
    var loaded = Map[String, Any]()

    //@todo slows down parsing 4 times, need better regex in better times
    value.replaceAll("""[\r\n{}]+""", "").trim().split(",").filter(_.nonEmpty).map(x => {
      val t = x.split("\":")
      //@todo these two regexes are bad, should be done on value beforehand
      val v = t(1).replaceAll("""^[ \t]+|[ \t]+$""", "")
      val k = t(0).replaceAll("""^[\"\' \t]+|[\"\' \t]+$""", "")
      loaded = char2fn.getOrElse(v(0), pass)(k, v, loaded)
    })
    loaded
  }
}

package com.github.jancajthaml.json

/**
  * Preallocated lookup variables for json dumps/loads
  *
  * @author jan.cajthaml
  */
private[json] object json {

  //@todo shorten variables names
  val nothing: Vector[Any] = Vector('"', ':', 'n', 'u', 'l', 'l', ',')
  val curlystart: Vector[Any] = Vector('{')
  val curlyendcolon: Vector[Any] = Vector('}', ',')
  val quotecomma: Vector[Any] = Vector('"', ',')
  val quotecolonquote: Vector[Any] = Vector('"', ':', '"')
  val quotecolon: Vector[Any] = Vector('"', ':')
  val colon: Vector[Any] = Vector(',')
  val quote: Vector[Any] = Vector('"')
  val pass = ((k: String, v: String, r: Map[String, Any]) => r)

  /*
    (t|f) => boolean (true|false)
    (0|1|2|3|4|5|6|7|8|9) => integral number
    (") => string
    (-) => negative integral number
    (n) => null
    (u) => undefined or unicode ... skip
    (e) => decimal number sci notation ... skip
  */
  val char2fn = Map(
    '"' -> ((k: String, v: String, r: Map[String, Any]) => r + (k -> v.drop(1).dropRight(1))),
    't' -> ((k: String, v: String, r: Map[String, Any]) => r + (k -> true)),
    'f' -> ((k: String, v: String, r: Map[String, Any]) => r + (k -> false)),
    'n' -> ((k: String, v: String, r: Map[String, Any]) => r + (k -> null)),
    '0' -> ((k: String, v: String, r: Map[String, Any]) => r + (k -> 0)),
    '1' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '2' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '3' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '4' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '5' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '6' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '7' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '8' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '9' -> ((k: String, v: String, r: Map[String, Any]) => if (v.toLong > Integer.MAX_VALUE) r + (k -> v.toLong) else r + (k -> v.toInt)),
    '-' -> ((k: String, v: String, r: Map[String, Any]) => r + (k -> Map(
      '0' -> ((v: String) => 0),
      '1' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '2' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '3' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '4' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '5' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '6' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '7' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '8' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt),
      '9' -> ((v: String) => if (v.toLong < Integer.MIN_VALUE) v.toLong else v.toInt)
    )(v(1))(v)))
  )

}

object jsondumps extends (Map[String, Any] => String) {
  //@todo shorten variables names
  import json.{
    nothing,
    quotecomma,
    quotecolonquote,
    quotecolon,
    curlystart,
    curlyendcolon,
    colon,
    quote
  }

  def apply(value: Map[String, Any]): String = {
    def walk(value: Map[String, Any]): Vector[Any] = {
      var vector: Vector[Any] = Vector[Any]()
      value.map(x => x._2 match {
        case v: String => vector ++= (quote ++ x._1 ++ quotecolonquote ++ v ++ quotecomma)
        case null => vector ++= (quote ++ x._1 ++ nothing)
        case (v: Map[String, Any] @unchecked) => vector ++= (quote ++ x._1 ++ quotecolon ++ walk(v))
        case v => vector ++= (quote ++ x._1 ++ quotecolon ++ v.toString ++ colon)
      })
      curlystart ++ vector.dropRight(1) ++ curlyendcolon
    }
    walk(value).dropRight(1).mkString
  }
}

object jsonloads extends (String => Map[String, Any]) {

  import json.{char2fn, pass}

  def apply(value: String): Map[String, Any] = {
    //@todo check string contains nested json (more than one "{" or "}")
    //because we do not support nested json strucures @todo TBD
    var loaded = Map[String, Any]()

    //[\s]+{|}[\s]+|[\r\n{}]+|\s(?=([^"]*"[^"]*")*[^"]*$)
    value.trim().replaceAll("""[\r\n{}]+|\s(?=([^"]*"[^"]*")*[^"]*$)""", "").split(",").filter(_.nonEmpty).map(x => {
      //@todo instead of split(",") and then split (":") we can either use indexOf and work with substring
      //(not copying resources) or we can use regex match groups thus removing isEmpty and splitting by ","
      //faster
      val t = x.split("\":")
      val v = t(1)
      //@todo this regex could be done before iteration thus speeding up process even more
      val k = t(0).replaceAll("""^[\"]+|[\"]+$""", "")
      loaded = char2fn.getOrElse(v(0), pass)(k, v, loaded)
    })
    loaded
  }
}

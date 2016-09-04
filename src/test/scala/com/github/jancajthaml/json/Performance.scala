package com.github.jancajthaml.json

import org.scalameter.api._
import org.scalameter.picklers.Implicits._

object Regression extends Bench[Double] {
  
  /* configuration */

  lazy val executor = LocalExecutor(
    new Executor.Warmer.Default,
    Aggregator.min[Double],
    measurer
  )
  
  lazy val measurer = new Measurer.Default
  lazy val reporter = new LoggingReporter[Double]
  lazy val persistor = Persistor.None
  
  val numberOfKeys = Gen.range("numberOfKeys")(0, 500, 50)
  val maps = for (sz <- numberOfKeys) yield Map((0 until sz).toList map { a => s"$a" -> a }: _*)
  val jsons = for (sz <- numberOfKeys) yield jsondumps(Map((0 until sz).toList map { a => s"$a" -> s"$a" }: _*))
  
  /* tests */

  performance of "com.github.jancajthaml.json" in {
    measure method "jsondumps" in {
      using(maps) in jsondumps
    }
    measure method "jsonloads" in {
      using(jsons) in jsonloads
    }
  }
  
}
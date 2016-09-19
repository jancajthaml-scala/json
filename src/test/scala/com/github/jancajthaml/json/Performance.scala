package com.github.jancajthaml.json

import org.scalameter.api.{Bench, Gen, exec}

object Regression extends Bench.OfflineReport {
  
  val numberOfKeys = Gen.range("numberOfKeys")(0, 500, 50)
  val maps = for (sz <- numberOfKeys) yield Map((0 until sz).toList map { a => s"$a" -> a }: _*)
  val jsons = for (sz <- numberOfKeys) yield jsondumps(Map((0 until sz).toList map { a => s"$a" -> s"$a" }: _*))
  
  /* tests */

  performance of "com.github.jancajthaml.json" in {
    measure method "jsondumps" in {
      using(maps) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in {x => jsondumps(x)}
    }
    measure method "jsonloads" in {
      using(jsons) config (
        exec.benchRuns -> 20,
        exec.independentSamples -> 1,
        exec.outliers.covMultiplier -> 1.5,
        exec.outliers.suspectPercent -> 40
      ) in {x => jsonloads(x)}
    }
  }
  
}
package br.com.idp.models

import br.com.idp.SparkTestWrapper
import org.scalatest.flatspec.AnyFlatSpec

class ConfigBronzeTest extends AnyFlatSpec with SparkTestWrapper {

  "ConfigBronze.readConfig" should "should parse file" in {
    val config = ConfigBronze.readConfig(spark.sparkContext, "src/test/resources/bronze_config.json")

    assert(config.folders.size > 0)
  }

}

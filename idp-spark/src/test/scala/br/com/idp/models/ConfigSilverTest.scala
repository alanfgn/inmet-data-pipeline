package br.com.idp.models

import br.com.idp.SparkTestWrapper
import org.scalatest.flatspec.AnyFlatSpec

class ConfigSilverTest extends AnyFlatSpec with SparkTestWrapper {

  "ConfigSilver.readConfig" should "should parse file" in {
    val config = ConfigSilver.readConfig(spark.sparkContext, "src/test/resources/silver_config.json")

    assert(config.tableStructure.size > 0)
  }

}

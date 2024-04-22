package br.com.idp.models

import br.com.idp.SparkTestWrapper
import org.scalatest.flatspec.AnyFlatSpec

class  ConfigGoldTest extends AnyFlatSpec with SparkTestWrapper {

  "ConfigGold.readConfig" should "should parse file" in {
    val config = ConfigGold.readConfig(spark.sparkContext, "src/test/resources/gold_config.json")
    assert(config.outputs.size > 0)

  }

}

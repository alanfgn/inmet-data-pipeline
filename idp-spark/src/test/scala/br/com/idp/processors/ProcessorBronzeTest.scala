package br.com.idp.processors

import br.com.idp.SparkTestWrapper
import br.com.idp.models.ConfigBronze
import org.scalatest.flatspec.AnyFlatSpec

class ProcessorBronzeTest extends AnyFlatSpec with SparkTestWrapper {

  "ProcessorBronze.run" should "should run files" in {
    val config = ConfigBronze.readConfig(spark.sparkContext, "src/test/resources/bronze_config.json")
    ProcessorBronze.run(spark, config)

  }
}

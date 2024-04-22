package br.com.idp.processors

import br.com.idp.SparkTestWrapper
import br.com.idp.models.ConfigSilver
import org.scalatest.flatspec.AnyFlatSpec

class ProcessorSilverTest  extends AnyFlatSpec with SparkTestWrapper {

  "ProcessorSilver.run" should "should run files" in {

    val config = ConfigSilver.readConfig(spark.sparkContext, "src/test/resources/silver_config.json")
    ProcessorSilver.run(spark, config)

  }
}

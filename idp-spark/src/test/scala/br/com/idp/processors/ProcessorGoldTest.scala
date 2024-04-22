package br.com.idp.processors

import br.com.idp.SparkTestWrapper
import br.com.idp.args.GoldParameters
import br.com.idp.models.ConfigGold
import org.apache.commons.io.FileUtils
import org.apache.hadoop.fs.FileSystem
import org.scalatest.flatspec.AnyFlatSpec

import java.io.File

class ProcessorGoldTest extends AnyFlatSpec with SparkTestWrapper {

  "ProcessorGold.run" should "should run files" in {

    FileUtils.deleteDirectory(new File("src/test/resources/gold"))

    val config = ConfigGold.readConfig(spark.sparkContext, "src/test/resources/gold_config.json")
    ProcessorGold.run(spark, config, GoldParameters(), "parquet")

  }
}

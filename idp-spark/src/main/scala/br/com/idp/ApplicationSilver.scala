package br.com.idp

import br.com.idp.args.SilverParameters
import br.com.idp.models.ConfigSilver
import br.com.idp.processors.ProcessorSilver
import br.com.idp.utils.SessionFactory

object ApplicationSilver extends App {
  val parameters = SilverParameters.parseArgs(args)
  val spark = SessionFactory.makeSparkSession("Puc Minas INMET Silver App")
  val config = ConfigSilver.readConfig(spark.sparkContext, parameters.configFile)

  ProcessorSilver.run(spark, config)
}

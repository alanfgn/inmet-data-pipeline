package br.com.idp

import br.com.idp.args.GoldParameters
import br.com.idp.models.ConfigGold
import br.com.idp.processors.ProcessorGold
import br.com.idp.utils.SessionFactory

object ApplicationGold extends App {
 val parameters = GoldParameters.parseArgs(args)
 val spark = SessionFactory.makeSparkSession("Puc Minas INMET Gold App")
 val config = ConfigGold.readConfig(spark.sparkContext, parameters.configFile)

 ProcessorGold.run(spark, config, parameters)
}

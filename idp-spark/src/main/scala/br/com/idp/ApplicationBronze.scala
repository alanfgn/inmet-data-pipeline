package br.com.idp

import br.com.idp.args.BronzeParameters
import br.com.idp.models.ConfigBronze
import br.com.idp.processors.ProcessorBronze
import br.com.idp.utils.SessionFactory

object ApplicationBronze extends App {
 val parameters = BronzeParameters.parseArgs(args)
 val spark = SessionFactory.makeSparkSession("Puc Minas INMET Bronze App")
 val config = ConfigBronze.readConfig(spark.sparkContext, parameters.configFile)

 ProcessorBronze.run(spark, config)
}

package br.com.idp.args

case class BronzeParameters(
 configFile: String = ""
)


object BronzeParameters {

  def parseArgs(args: Seq[String]): BronzeParameters = {

    val parser = new scopt.OptionParser[BronzeParameters]("INMET Bronze Process") {
      head("INMET Bronze Process", "4.x")

      opt[String]("config-file")
        .action((x, c) => c.copy(configFile = x))
        .text("Bronze Config File")
        .required()
    }

    parser.parse(args, BronzeParameters()) match {
      case Some(parameters) => parameters
      case _ => throw new IllegalArgumentException("Error Parsing Parameters")
    }
  }
}
package br.com.idp.args

case class SilverParameters(
  configFile: String = ""
)

object SilverParameters {

  def parseArgs(args: Seq[String]): SilverParameters = {

    val parser = new scopt.OptionParser[SilverParameters]("INMET Silver Process") {
      head("INMET Silver Process", "4.x")

      opt[String]("config-file")
        .action((x, c) => c.copy(configFile = x))
        .text("Silver Config File")
        .required()
    }

    parser.parse(args, SilverParameters()) match {
      case Some(parameters) => parameters
      case _ => throw new IllegalArgumentException( "Error Parsing Parameters")
    }
  }
}
package br.com.idp.args

case class GoldParameters(
  configFile: String = "",
  jdbcDriver: String = "org.postgresql.Driver",
  jdbcUrl: String = "",
  jdbcUser: String = "",
  jdbcPassword: String = ""
)

object GoldParameters {

  def parseArgs(args: Seq[String]): GoldParameters = {

    val parser = new scopt.OptionParser[GoldParameters]("INMET Gold Process") {
      head("INMET Gold Process", "4.x")

      opt[String]("config-file")
        .action((x, c) => c.copy(configFile = x))
        .text("Gold Config File")
        .required()

      opt[String]("jdbc-driver")
        .action((x, c) => c.copy(jdbcDriver = x))
        .text("Jdbc Driver")

      opt[String]("jdbc-url")
        .action((x, c) => c.copy(jdbcUrl = x))
        .text("Jdbc Url")
        .required()

      opt[String]("jdbc-user")
        .action((x, c) => c.copy(jdbcUser = x))
        .text("Jdbc User")
        .required()

      opt[String]("jdbc-password")
        .action((x, c) => c.copy(jdbcPassword = x))
        .text("Jdbc Password")
        .required()
    }

    parser.parse(args, GoldParameters()) match {
      case Some(parameters) => parameters
      case _ => throw new IllegalArgumentException("Error Parsing Parameters")
    }
  }
}

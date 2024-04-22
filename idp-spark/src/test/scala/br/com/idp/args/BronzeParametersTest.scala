package br.com.idp.args

import org.scalatest.flatspec.AnyFlatSpec

class BronzeParametersTest extends AnyFlatSpec  {

  "BronzeParameters.parseArgs" should "should parse argsminets" in {
    val parameters = BronzeParameters.parseArgs(Seq(
      "--config-file", "test_conf.json"
    ))

    assert(parameters.configFile.equals("test_conf.json"))
  }
}

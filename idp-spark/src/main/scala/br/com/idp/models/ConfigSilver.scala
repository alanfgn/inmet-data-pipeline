package br.com.idp.models

import io.circe.generic.auto._
import io.circe.parser
import org.apache.spark.SparkContext

case class ColumnDefinitionSilver (
  columnName: String,
  columnType: String,
  nullable: Option[Boolean],
  fromMetadata: Option[String],
  value: Option[String]
) extends ColumnDefinition

case class ConfigSilverOutput(
  path: String,
  partitionBy: Seq[String]
)

case class ConfigSilverNormalization(
  normalizationType: String,
  parameters: Option[Seq[String]],
  columns: Seq[String]
)

case class ConfigSilverInput (
  name: String,
  inputPath: String,
  metadataPath: String,
  tablePath: String,
)

case class ConfigSilver (
  inputs: Seq[ConfigSilverInput],
  output: ConfigSilverOutput,
  normalizations: Seq[ConfigSilverNormalization],
  tableStructure: Seq[ColumnDefinitionSilver]
)

object ConfigSilver {
  def readConfig(sc: SparkContext, configPath: String): ConfigSilver = {
    val content: String = sc.textFile(configPath).collect().mkString

    parser.decode[ConfigSilver](content) match {
      case  Right(config) => config
      case  Left(error) => throw new RuntimeException(s"Error parsing Silver Config in path ${configPath}", error)
    }
  }

}
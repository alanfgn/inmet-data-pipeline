package br.com.idp.models

import io.circe.generic.auto._
import io.circe.parser
import org.apache.spark.SparkContext

case class ColumnDefinitionGold (
  columnName: String,
  columnType: String,
  nullable: Option[Boolean],
  value: Option[String]
) extends ColumnDefinition

case class ConfigGoldOperation (
   operationType: String,
   parameters: Option[Seq[String]],
   columns: Seq[String]
)

case class ConfigGoldOutput (
  name: String,
  path: String,
  operations: Option[Seq[ConfigGoldOperation]],
  columns: Seq[ColumnDefinitionGold]
)

case class ConfigGold (
  inputPath: String,
  outputs: Seq[ConfigGoldOutput]
)

object ConfigGold {
  def readConfig(sc: SparkContext, configPath: String): ConfigGold = {
    val content: String = sc.textFile(configPath).collect().mkString

    parser.decode[ConfigGold](content) match {
      case  Right(config) => config
      case  Left(error) => throw new RuntimeException(s"Error parsing Gold Config in path ${configPath}", error)
    }
  }
}
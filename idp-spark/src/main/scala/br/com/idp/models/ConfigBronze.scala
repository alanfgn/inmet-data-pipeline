package br.com.idp.models

import io.circe.generic.auto._
import io.circe.parser
import org.apache.spark.SparkContext

case class ColumnDefinitionBronze (
  columnName: String,
  columnType: String,
  nullable: Option[Boolean],
  regexpReplace: Option[Seq[(String, String)]],
  toDateFormat: Option[String]
) extends ColumnDefinition

case class BronzeMetadataStructure(
  lines: Int,
  values: Seq[ColumnDefinitionBronze]
)

case class BronzeFolderDefinition(
  name: String,
  inputPath: String,
  outputPath: String
)

case class ConfigBronze(
  folders: Seq[BronzeFolderDefinition],
  metadataStructure: BronzeMetadataStructure,
  tableStructure: Seq[ColumnDefinitionBronze]
)

object ConfigBronze {

  def readConfig(sc: SparkContext, configPath: String): ConfigBronze = {
      val content: String = sc.textFile(configPath).collect().mkString

      parser.decode[ConfigBronze](content) match {
        case  Right(config) => config
        case  Left(error) => throw new RuntimeException(s"Error parsing Bonze Config in path ${configPath}", error)
      }
  }

}



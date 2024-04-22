package br.com.idp.processors

import br.com.idp.models.{ConfigSilver, ConfigSilverNormalization}
import io.circe.parser
import org.apache.commons.io.FileUtils
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.functions.{col, expr, lit, when}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import java.io.File

object ProcessorSilver extends Logging {

  def run(spark: SparkSession, config: ConfigSilver): Unit = {

    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)

    FileUtils.deleteDirectory(new File(config.output.path))

    config.inputs.foreach(input => {

      val inputPath = new Path(input.inputPath)
      val fsMetadata = inputPath.getFileSystem(fs.getConf)

      val files = fsMetadata.listStatus(inputPath)
      files.foreach(file => {
        val metadataPath = s"${file.getPath.toString}/${input.metadataPath}"
        val tablePath = s"${file.getPath.toString}/${input.tablePath}"

        val contentMetadata: String = spark.sparkContext.textFile(metadataPath).collect().mkString
        val metadata = parser.decode[Map[String, String]](contentMetadata) match {
          case  Right(config) => config
          case  Left(error) => throw new RuntimeException(s"Error parsing metadata ${contentMetadata}", error)
        }

        def normalizationToNull(normalization: ConfigSilverNormalization)(dataframe: DataFrame) = {
          normalization.columns.foldLeft(dataframe)((df, column) => {
            df.withColumn(column,
              when(expr(s"${column} in (${normalization.parameters.get.mkString(",")})"), lit(null))
                .otherwise(col(column)))
          })
        }

        def removeNulls(normalization: ConfigSilverNormalization)(dataFrame: DataFrame) = {
          normalization.columns.foldLeft(dataFrame)((df, column) => {
            df.where(col(column).isNotNull)
          })
        }

        val inputDf = spark.read.parquet(tablePath)

        val normalizedDf = config.normalizations
          .foldLeft(inputDf)((df, normalization) => {
            normalization.normalizationType match {
              case "to_null" => df.transform(normalizationToNull(normalization))
              case "remove_nulls" => df.transform(removeNulls(normalization))
              case _ => throw new RuntimeException(s"normalization not implemented")
            }
          })

        val enrichMetadataDf = config.tableStructure
          .filter(_.fromMetadata.isDefined)
          .foldLeft(normalizedDf)((df, column) => {df.withColumn(column.columnName, lit(metadata(column.fromMetadata.get)).cast(column.columnType) )})

        val enrichNewColumnsDf = config.tableStructure
          .filter(_.value.isDefined)
          .foldLeft(enrichMetadataDf)((df, column) => df.withColumn(column.columnName, expr(column.value.get).cast(column.columnType)))

        val finalDf = enrichNewColumnsDf.select(config.tableStructure.map(column => col(column.columnName).cast(column.columnType)):_*)

        finalDf.write
          .mode(SaveMode.Append)
          .partitionBy(config.output.partitionBy:_*)
          .parquet(config.output.path)
      })
    })

  }
}

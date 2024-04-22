package br.com.idp.processors

import br.com.idp.models.{ColumnDefinitionBronze, ConfigBronze}
import br.com.idp.utils.Utils
import io.circe.syntax._
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.functions.{col, regexp_replace, to_date}
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.io.BufferedOutputStream

object ProcessorBronze extends Logging {

  def run(spark: SparkSession, config: ConfigBronze): Unit = {

    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)

    config.folders.par.foreach(folder => {
      val files = Utils.listFilesInPath(fs, folder.inputPath)

      val schema = Utils.makeSchema(config.tableStructure)
      val stringSchema = Utils.makeStringSchema(config.tableStructure)

      files.par.foreach(file => {

        logInfo(s"ProcessorBronze.run files.foreach file ${file}")
        logInfo(s"ProcessorBronze.run files.foreach file.getPath ${file.getPath}")
        logInfo(s"ProcessorBronze.run files.foreach file.getPath.toUri ${file.getPath.toUri}")
        logInfo(s"ProcessorBronze.run files.foreach file.getPath.toUri.getPath ${file.getPath.toUri.getPath}")

        val zipFile = spark.read.format("csv")
          .option("header", false)
          .option("sep", ";")
          .option("encoding", "cp1252")
          .schema(stringSchema)
          .load(file.getPath.toString)

        val zipMetadata = zipFile.rdd
          .zipWithIndex()
          .filter(zip => {
          zip._2 < config.metadataStructure.lines
        })

        val zipTable = zipFile
          .transform(makeCorrections(config.tableStructure)).rdd
          .zipWithIndex()
          .filter(zip => {
          zip._2 > config.metadataStructure.lines + 1
        }).map(zip => zip._1)

        val metadata = zipMetadata.collect()
          .map(metadataInfo => {
            def columnDef = config.metadataStructure.values(metadataInfo._2.toInt)
            def columnValue = columnDef.regexpReplace
              .getOrElse(Seq.empty[(String, String)])
              .foldLeft(metadataInfo._1.getString(1))((value, regexReplace) => value.replace(regexReplace._1, regexReplace._2))

            columnDef.columnName ->  columnValue
          })
          .toMap.asJson.spaces2

        val baseFilePath = folder.outputPath + "/" + file.getPath.getName

        val metadataPath = new Path(baseFilePath + "/metadata.json")
        val fsMetadata = metadataPath.getFileSystem(fs.getConf)
        val metadataOut = new BufferedOutputStream(fsMetadata.create(metadataPath))
        metadataOut.write(metadata.getBytes("UTF-8"))
        metadataOut.flush()
        metadataOut.close()

        spark.createDataFrame(zipTable, schema)
          .write
          .mode("overwrite")
          .parquet(baseFilePath + "/table")
      })
    })
  }

  def makeCorrections(columns: Seq[ColumnDefinitionBronze])(dataframe: DataFrame): DataFrame = {
    columns.foldLeft(dataframe)((dfColumn, column) => {
      val df = if(column.toDateFormat.isDefined) {
        dfColumn.withColumn(column.columnName, to_date(col(column.columnName), column.toDateFormat.get))
      } else dfColumn

      column.regexpReplace.getOrElse(Seq.empty[(String, String)]).foldLeft(df)((dfCorrection, correction) => {
        dfCorrection.withColumn(column.columnName, regexp_replace(col(column.columnName), correction._1, correction._2))
      }).withColumn(column.columnName, col(column.columnName).cast(column.columnType))
    })
  }

}

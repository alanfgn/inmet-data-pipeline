package br.com.idp.processors

import br.com.idp.args.GoldParameters
import br.com.idp.models.{ConfigGold, ConfigGoldOperation, ConfigGoldOutput}
import org.apache.spark.sql.functions.{col, expr}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object ProcessorGold {

  def run(spark: SparkSession, config: ConfigGold, parameters: GoldParameters, outputType: String = "jdbc"): Unit = {

    val inputDf = spark.read.parquet(config.inputPath)

    config.outputs.foreach(output => {

      val outputDf = inputDf
        .transform(makeOperations(output.operations))
        .select(output.columns.map(c => expr(c.value.getOrElse(c.columnName)).cast(c.columnType).as(c.columnName)):_*)

      outputType match {
        case "jdbc" => writeJdbc(parameters, output, outputDf)
        case "parquet" => writeParquet(output, outputDf)
      }
    })
  }

  private def writeParquet(output: ConfigGoldOutput, outputDf: DataFrame) = {
    outputDf.write.parquet(output.path)
  }

  private def writeJdbc(parameters: GoldParameters, output: ConfigGoldOutput, outputDf: DataFrame) = {
    outputDf.write
      .format("jdbc")
      .mode(SaveMode.Overwrite)
      .option("driver", parameters.jdbcDriver)
      .option("url", parameters.jdbcUrl)
      .option("dbtable", output.path)
      .option("user", parameters.jdbcUser)
      .option("password", parameters.jdbcPassword)
      .save()
  }

  def operateGroupBy(df: DataFrame, operation: ConfigGoldOperation): DataFrame = {
    val columns = operation.columns.map(expr(_))
    df.groupBy(operation.parameters.get.map(col(_)):_*).agg(columns.head,columns.tail:_*)
  }

  def operateCreateColumns(df: DataFrame, operation: ConfigGoldOperation): DataFrame = {
    df.select((df.columns ++ operation.columns).map(expr(_)):_*)
  }

  def operateFilter(df: DataFrame, operation: ConfigGoldOperation): DataFrame = {
    operation.parameters.get.foldLeft(df)((dtf, filter) => dtf.where(filter))
  }

  def makeOperations(operations: Option[Seq[ConfigGoldOperation]])(df: DataFrame): DataFrame = {
    if(!operations.isDefined) return df

    operations.get.foldLeft(df)((df, operation) => {
      operation.operationType match {
        case "group_by" => operateGroupBy(df, operation)
        case "create_columns" => operateCreateColumns(df, operation)
        case "filter" => operateFilter(df, operation)
        case _ => throw  new RuntimeException("Operation not supported")
      }
    })
  }

}

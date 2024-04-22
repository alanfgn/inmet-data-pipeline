package br.com.idp

import org.apache.spark.sql.SparkSession

trait SparkTestWrapper {

  lazy val spark: SparkSession = {
    SparkSession.builder()
      .master("local[*]")
      .appName("Test Spark")
      .getOrCreate()
  }

}

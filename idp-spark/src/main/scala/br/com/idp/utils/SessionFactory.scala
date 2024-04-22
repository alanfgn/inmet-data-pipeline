package br.com.idp.utils

import org.apache.spark.sql.SparkSession

object SessionFactory {

  def makeSparkSession(name: String) = {
    SparkSession.builder()
      .appName(name)
      .getOrCreate()
  }

}

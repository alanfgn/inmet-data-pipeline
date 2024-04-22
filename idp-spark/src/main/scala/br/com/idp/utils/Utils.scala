package br.com.idp.utils

import br.com.idp.models.ColumnDefinition
import org.apache.hadoop.fs.{FileSystem, LocatedFileStatus, Path, RemoteIterator}
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser
import org.apache.spark.sql.types.{StructField, StructType}

object Utils {

  def listFilesInPath(fs: FileSystem, folderPath: String): Seq[LocatedFileStatus] = {
    val path = new Path(folderPath)
    val realFileSystem = path.getFileSystem(fs.getConf)

    val iteratorFiles = realFileSystem.listFiles(new Path(folderPath), false)
    remoteIteratorToSeq(iteratorFiles)
  }

  def remoteIteratorToSeq[T](iterator: RemoteIterator[T]): Seq[T] ={
    var seqFiles = Seq.empty[T]

    while (iterator.hasNext) {
      seqFiles = seqFiles :+ iterator.next()
    }

    seqFiles
  }

  def makeSchema(columns: Seq[ColumnDefinition], overrideType: Option[String] = None): StructType = {
    StructType.apply(columns.map(column => {
      StructField(column.columnName, CatalystSqlParser.parseDataType(overrideType.getOrElse(column.columnType)), column.nullable.getOrElse(true))
    }))
  }

  def makeStringSchema(columns: Seq[ColumnDefinition]): StructType  = makeSchema(columns, Some("string"))


}

package br.com.idp.models

trait ColumnDefinition {
  val columnName: String
  val columnType: String
  val nullable: Option[Boolean]
}
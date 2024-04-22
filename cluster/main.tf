module "airflow" {
  depends_on = [ module.spark ]
  source = "./modules/airflow"
}

module "spark" {
  source = "./modules/spark"
}

module "minio" {
  source = "./modules/minio"
}

module "grafana" {
  source = "./modules/grafana"
}

module "postgresql" {
  source = "./modules/postgresql"
}
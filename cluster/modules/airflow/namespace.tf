resource "kubernetes_namespace" "airflow-ns" {
  metadata {
    name = "airflow-ns"
  }
}
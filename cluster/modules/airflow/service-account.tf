resource "kubernetes_service_account" "airflow-sa" {
  depends_on = [kubernetes_namespace.airflow-ns]

  metadata {
    name      = "airflow-sa"
    namespace = "airflow-ns"
  }
  secret {
    name = "airflow-secret"
  }
}

resource "kubernetes_secret" "airflow-secret" {
  metadata {
    name = "airflow-secret"
  }
}

resource "kubernetes_service_account" "spark-operator-sa" {
  depends_on = [kubernetes_namespace.spark-ns]

  metadata {
    name      = "spark-operator-sa"
    namespace = "spark-ns"
  }
  secret {
    name = "spark-operator-secret"
  }
}

resource "kubernetes_secret" "spark-operator-secret" {
  metadata {
    name = "spark-operator-secret"
  }
}


resource "kubernetes_service_account" "spark-apps-sa" {
  depends_on = [kubernetes_namespace.spark-ns]

  metadata {
    name      = "spark-apps-sa"
    namespace = "spark-ns"
  }
  secret {
    name = "spark-apps-secret"
  }
}

resource "kubernetes_secret" "spark-apps-secret" {
  metadata {
    name = "spark-apps-secret"
  }
}
resource "kubernetes_namespace" "spark-ns" {
  metadata {
    name = "spark-ns"
  }
}
resource "kubernetes_namespace" "postgresql-ns" {
  metadata {
    name = "postgresql-ns"
  }
}

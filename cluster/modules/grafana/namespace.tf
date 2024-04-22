resource "kubernetes_namespace" "grafana-ns" {
  metadata {
    name = "grafana-ns"
  }
}


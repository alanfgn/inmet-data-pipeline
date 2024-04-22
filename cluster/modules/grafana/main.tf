resource "helm_release" "grafana" {

  depends_on = [kubernetes_namespace.grafana-ns]

  name       = "grafana"
  repository = "https://grafana.github.io/helm-charts"
  chart      = "grafana"
  namespace  = "grafana-ns"
  wait       = true

}

resource "helm_release" "spark" {
  depends_on = [kubernetes_namespace.spark-ns, kubernetes_service_account.spark-operator-sa, kubernetes_service_account.spark-apps-sa]

  name = "spark"

  repository = "https://kubeflow.github.io/spark-operator"
  version    = "1.1.27"
  chart      = "spark-operator"
  namespace  = "spark-ns"
  wait       = true

  set {
    name  = "serviceAccounts.sparkoperator.name"
    value = "spark-operator-sa"
  }

  set {
    name  = "serviceAccounts.sparkoperator.create"
    value = "false"
  }

  set {
    name  = "serviceAccounts.spark.name"
    value = "spark-apps-sa"
  }

  set {
    name  = "serviceAccounts.spark.create"
    value = "false"
  }
}

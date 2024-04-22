resource "kubernetes_cluster_role_binding" "role-binding-spark-operator" {
  depends_on = [kubernetes_namespace.spark-ns]

  metadata {
    name = "role-binding-spark-operator"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "cluster-admin"
  }

  subject {
    kind      = "ServiceAccount"
    name      = "spark-operator-sa"
    namespace = "spark-ns"
  }
}

resource "kubernetes_cluster_role_binding" "role-binding-spark-apps" {
  depends_on = [kubernetes_namespace.spark-ns]

  metadata {
    name = "role-binding-spark-apps"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "cluster-admin"
  }

  subject {
    kind      = "ServiceAccount"
    name      = "spark-apps-sa"
    namespace = "spark-ns"
  }
}
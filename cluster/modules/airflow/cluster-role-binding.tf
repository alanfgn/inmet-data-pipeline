resource "kubernetes_cluster_role_binding" "role-binding-airflow-operator" {
  depends_on = [kubernetes_namespace.airflow-ns ]

  metadata {
    name = "role-binding-airflow-operator"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "cluster-admin"
  }
  
  subject {
    kind      = "ServiceAccount"
    name      = "airflow-sa"
    namespace = "airflow-ns"
  }
}
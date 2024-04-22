resource "kubernetes_role" "role-spark-operator" {

  metadata {
    name      = "role-spark-operator"
    namespace = "spark-ns"
  }

  rule {
    api_groups = ["sparkoperator.k8s.io"]
    resources  = ["sparkapplications"]
    verbs      = ["create", "get", "list", "watch"]
  }
}

resource "kubernetes_role_binding" "role-binding-airflow-spark" {

  metadata {
    name      = "role-binding-airflow-spark"
    namespace = "spark-ns"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "Role"
    name      = "role-spark-operator"
  }

  subject {
    kind      = "ServiceAccount"
    name      = "airflow-sa"
    namespace = "airflow-ns"
  }
}
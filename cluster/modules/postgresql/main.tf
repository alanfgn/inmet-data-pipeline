resource "helm_release" "postgresql" {

  depends_on = [kubernetes_namespace.postgresql-ns]

  name       = "postgresql"
  repository = "oci://registry-1.docker.io/bitnamicharts/"
  chart      = "postgresql"
  namespace  = "postgresql-ns"
  wait       = true

  set {
    name  = "auth.postgresPassword"
    value = "idp-postregresql-password"
  }

  set {
    name  = "auth.database"
    value = "idp_database"
  }

}

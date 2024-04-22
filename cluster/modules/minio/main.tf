resource "helm_release" "minio" {

  depends_on = [kubernetes_namespace.minio-ns]

  version     = "v5.0.13"
  name        = "minio"
  repository  = "https://operator.min.io"
  chart       = "operator"
  namespace   = "minio-ns"
  wait        = true

  set {
    name  = "accessKey"
    value = "accessKey"
  }

  set {
    name  = "secretKey"
    value = "secretKey"
  }
}

resource "helm_release" "tenant" {

  depends_on = [kubernetes_namespace.tenant-ns, helm_release.minio]

  version     = "v5.0.13"
  name       = "minio-tenant"
  repository = "https://operator.min.io"
  chart      = "tenant"
  namespace  = "tenant-ns"
  wait       = true


  values = [
    file("${path.module}/tenant.yaml")
  ]

}

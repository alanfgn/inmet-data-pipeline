resource "kubernetes_namespace" "minio-ns" {
  metadata {
    name = "minio-ns"
  }
}

resource "kubernetes_namespace" "tenant-ns" {
  metadata {
    name = "tenant-ns"
  }
}
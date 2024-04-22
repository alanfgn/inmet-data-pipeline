resource "helm_release" "airflow" {
  depends_on = [kubernetes_namespace.airflow-ns, kubernetes_service_account.airflow-sa]

  name = "airflow"

  repository = "https://airflow.apache.org"
  chart      = "airflow"
  version    = "1.11.0"
  namespace  = "airflow-ns"
  wait       = true


  values = [
    file("${path.module}/airflow.yaml")
  ]

  set {
    name  = "executor"
    value = "CeleryExecutor"
  }

  set {
    name  = "webserver.defaultUser.password"
    value = "idp-password"
  }

  set {
    name  = "images.airflow.repository"
    value = "idp-airflow"
  }

  set {
    name  = "images.airflow.tag"
    value = "1.0.0"
  }

  set {
    name  = "createUserJob.useHelmHooks"
    value = "false"
  }

  set {
    name  = "createUserJob.applyCustomEnv"
    value = "false"
  }

  set {
    name  = "migrateDatabaseJob.useHelmHooks"
    value = "false"
  }

  set {
    name  = "migrateDatabaseJob.applyCustomEnv"
    value = "false"
  }

  set {
    name  = "scheduler.serviceAccount.create"
    value = "false"
  }

  set {
    name  = "scheduler.serviceAccount.name"
    value = "airflow-sa"
  }


  set {
    name  = "webserver.serviceAccount.create"
    value = "false"
  }

  set {
    name  = "webserver.serviceAccount.name"
    value = "airflow-sa"
  }


  set {
    name  = "workers.serviceAccount.create"
    value = "false"
  }

  set {
    name  = "workers.serviceAccount.name"
    value = "airflow-sa"
  }

    set {
    name  = "triggerer.serviceAccount.create"
    value = "false"
  }

  set {
    name  = "triggerer.serviceAccount.name"
    value = "airflow-sa"
  }

  set {
    name  = "triggerer.serviceAccount.create"
    value = "false"
  }

  set {
    name  = "triggerer.serviceAccount.name"
    value = "airflow-sa"
  }


  set {
    name  = "dagProcessor.serviceAccount.create"
    value = "false"
  }

  set {
    name  = "dagProcessor.serviceAccount.name"
    value = "airflow-sa"
  }

  set {
    name  = "createUserJob.serviceAccount.create"
    value = "false"
  }

  set {
    name  = "createUserJob.serviceAccount.name"
    value = "airflow-sa"
  }

}

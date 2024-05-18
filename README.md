# Inmet Data Pipeline (IDP)

This project is a case study for undergraduate dissertation of the postgraduate course in Data Engineering at the Pontifical Catholic University of Minas Gerais.

The data used for this pipeline was provided by INMET and can be found at https://bdmep.inmet.gov.br/

Below is the list of technologies used for implementation.

- [Terraform](https://www.terraform.io/)
- [Helm](https://helm.sh/)
    - [Airflow Chart](https://airflow.apache.org/docs/helm-chart/stable/index.html)
    - [Spark Chart](https://kubeflow.github.io/spark-operator/)
    - [Minio Chart](https://github.com/minio/operator)
    - [Postgresql Chart](https://github.com/bitnami/charts/blob/main/bitnami/postgresql/README.md)
    - [Grafana Chart](https://github.com/grafana/helm-charts/tree/main)
- [Kubernetes](https://kubernetes.io/)
- [Minikube](https://minikube.sigs.k8s.io/docs/)
- [Apache Airflow](https://airflow.apache.org/)
- [Apache Spark](https://spark.apache.org/)
- [Minio](https://min.io/)
- [Postgresql](https://www.postgresql.org/)
- [Grafana](https://grafana.com/)

## How to start enviroment with Minikube

```sh
make start-minikube 

eval $(minikube docker-env)

make start
```

## How to Access available consoles


- Airflow - http://localhost:8080/ 
    - user: admin 
    - password: idp-password 
- Minio-tenant - http://localhost:9091/
    - user: idp-tenant-access
    - password: idp-tenant-secret
- Grafana - http://localhost:3000/
    - user: admin
    - password: [get by `make get-grafana-secret`]


## How to configure correct Datasource in Grafana

**Name**
`grafana-postgresql-datasource`

**Host URL**
`postgresql.postgresql-ns.svc.cluster.local:5432`

**Database name**
`postgres`

**Username**
`postgres`

**Password**
`idp-postregresql-password`

**TLS/SSL Mode**
`Disable`

The grafana file to import is in `/configs/grafana-dashboard.json`

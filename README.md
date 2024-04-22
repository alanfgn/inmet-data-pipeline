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

make start-minukbe 

eval $(minikube docker-env)

make start

```



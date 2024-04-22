from airflow import DAG
from datetime import datetime
from airflow.providers.cncf.kubernetes.operators.spark_kubernetes import SparkKubernetesOperator
from airflow.providers.cncf.kubernetes.sensors.spark_kubernetes import SparkKubernetesSensor

default_args = {
    'owner': 'Alan',
    'depends_on_past': False,
    'start_date': datetime(2020, 1, 1)
}

arguments_to_pass = {
    's3a_access_key': "idp-tenant-access",
    's3a_secret_key': "idp-tenant-secret",
    'jdbc_driver': 'org.postgresql.Driver',
    'jdbc_url': 'jdbc:postgresql://postgresql.postgresql-ns.svc.cluster.local:5432/idp_database',
    'jdbc_user': 'postgres',
    'jdbc_password': 'idp-postregresql-password',
    'config_file': "s3a://idp-config/gold_config.json"
}

dag = DAG(
    "idp_batch_gold",
    description="ETL Batch gold",
    default_args=default_args,
    schedule_interval=None,
    tags=['puc-minas', 'inmet', 'idp', 'batch', 'gold'],
    catchup=False
)

etl_batch_spark_operator = SparkKubernetesOperator(
    dag=dag,
    task_id='etl_batch_spark_operator',
    namespace='spark-ns',
    application_file='resources/idp-spark-gold.yaml',
    kubernetes_conn_id='kubernetes_idp',
    do_xcom_push=True,
    params=arguments_to_pass)

monitor_spark_app_status = SparkKubernetesSensor(
    dag=dag,
    task_id='monitor_spark_app_status',
    namespace="spark-ns",
    application_name="{{ task_instance.xcom_pull(task_ids='etl_batch_spark_operator')['metadata']['name'] }}",
    kubernetes_conn_id="kubernetes_idp")


etl_batch_spark_operator >> monitor_spark_app_status

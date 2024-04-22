from airflow import DAG
from datetime import datetime
from airflow.providers.cncf.kubernetes.operators.spark_kubernetes import SparkKubernetesOperator
from airflow.providers.cncf.kubernetes.sensors.spark_kubernetes import SparkKubernetesSensor
from airflow.operators.trigger_dagrun import TriggerDagRunOperator

default_args = {
    'owner': 'Alan',
    'depends_on_past': False,
    'start_date': datetime(2020, 1, 1)
}

arguments_to_pass = {
    's3a_access_key': "idp-tenant-access",
    's3a_secret_key': "idp-tenant-secret",
    'config_file': "s3a://idp-config/silver_config.json"
}

dag = DAG(
    "idp_batch_silver",
    description="ETL Batch silver",
    default_args=default_args,
    schedule_interval=None,
    tags=['puc-minas', 'inmet', 'idp', 'batch', 'silver'],
    catchup=False
)

etl_batch_spark_operator = SparkKubernetesOperator(
    dag=dag,
    task_id='etl_batch_spark_operator',
    namespace='spark-ns',
    application_file='resources/idp-spark-silver.yaml',
    kubernetes_conn_id='kubernetes_idp',
    do_xcom_push=True,
    params=arguments_to_pass)

monitor_spark_app_status = SparkKubernetesSensor(
    dag=dag,
    task_id='monitor_spark_app_status',
    namespace="spark-ns",
    application_name="{{ task_instance.xcom_pull(task_ids='etl_batch_spark_operator')['metadata']['name'] }}",
    kubernetes_conn_id="kubernetes_idp")

trigger_gold = TriggerDagRunOperator(
    dag=dag,
    task_id='trigger_gold',
    trigger_dag_id="idp_batch_gold"
)

etl_batch_spark_operator >> monitor_spark_app_status >> trigger_gold

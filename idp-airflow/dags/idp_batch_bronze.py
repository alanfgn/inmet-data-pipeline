from airflow import DAG
from datetime import datetime
from airflow.providers.cncf.kubernetes.operators.spark_kubernetes import SparkKubernetesOperator
from airflow.providers.cncf.kubernetes.sensors.spark_kubernetes import SparkKubernetesSensor
from airflow.operators.trigger_dagrun import TriggerDagRunOperator
from airflow.operators.empty import EmptyOperator


default_args = {
    'owner': 'Alan',
    'depends_on_past': False,
    'start_date': datetime(2020, 1, 1)
}

arguments_to_pass_bronze_1 = {
    's3a_access_key': "idp-tenant-access",
    's3a_secret_key': "idp-tenant-secret",
    'config_file': "s3a://idp-config/bronze_config_2013_2018.json"
}

arguments_to_pass_bronze_2 = {
    's3a_access_key': "idp-tenant-access",
    's3a_secret_key': "idp-tenant-secret",
    'config_file': "s3a://idp-config/bronze_config_2019-2023.json"
}


dag = DAG(
    "idp_batch_bronze",
    description="ETL Batch Bronze",
    default_args=default_args,
    schedule_interval=None,
    tags=['puc-minas', 'inmet', 'idp', 'batch', 'bronze'],
    catchup=False
)


start = EmptyOperator(
    dag=dag,
    task_id='start'
)

etl_batch_spark_operator_bronze_1 = SparkKubernetesOperator(
    dag=dag,
    task_id='etl_batch_spark_operator_bronze_1',
    namespace='spark-ns',
    application_file='resources/idp-spark-bronze.yaml',
    kubernetes_conn_id='kubernetes_idp',
    do_xcom_push=True,
    params=arguments_to_pass_bronze_1)

monitor_spark_app_status_bronze_1 = SparkKubernetesSensor(
    dag=dag,
    task_id='monitor_spark_app_status_bronze_1',
    namespace="spark-ns",
    application_name="{{ task_instance.xcom_pull(task_ids='etl_batch_spark_operator_bronze_1')['metadata']['name'] }}",
    kubernetes_conn_id="kubernetes_idp")

etl_batch_spark_operator_bronze_2 = SparkKubernetesOperator(
    dag=dag,
    task_id='etl_batch_spark_operator_bronze_2',
    namespace='spark-ns',
    application_file='resources/idp-spark-bronze.yaml',
    kubernetes_conn_id='kubernetes_idp',
    do_xcom_push=True,
    params=arguments_to_pass_bronze_2)

monitor_spark_app_status_bronze_2 = SparkKubernetesSensor(
    dag=dag,
    task_id='monitor_spark_app_status_bronze_2',
    namespace="spark-ns",
    application_name="{{ task_instance.xcom_pull(task_ids='etl_batch_spark_operator_bronze_2')['metadata']['name'] }}",
    kubernetes_conn_id="kubernetes_idp")


trigger_silver = TriggerDagRunOperator(
    dag=dag,
    task_id='trigger_silver',
    trigger_dag_id="idp_batch_silver"
)

start >> etl_batch_spark_operator_bronze_1 >> monitor_spark_app_status_bronze_1 >> trigger_silver
start >> etl_batch_spark_operator_bronze_2 >> monitor_spark_app_status_bronze_2 >> trigger_silver

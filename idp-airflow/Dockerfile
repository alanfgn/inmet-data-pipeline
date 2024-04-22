FROM apache/airflow:2.7.1

COPY --chown=airflow:root ./dags/ /opt/airflow/dags

COPY requirements.txt /
RUN pip install --no-cache-dir "apache-airflow==${AIRFLOW_VERSION}" -r /requirements.txt
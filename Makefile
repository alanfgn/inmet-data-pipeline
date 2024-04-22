start-minikube:
	minikube start --cpus 6 --memory 8192

start:
	$(MAKE) build-airflow-image
	$(MAKE) build-spark-image
	$(MAKE) start-enviroment
	$(MAKE) expose-all
	$(MAKE) load-data

end:
	terraform -chdir=cluster/ destroy

restart:
	$(MAKE) end
	$(MAKE) start

expose-all:
	$(MAKE) expose-airflow &
	$(MAKE) expose-minio &
	$(MAKE) expose-tenant &
	$(MAKE) expose-tenant-console &
	$(MAKE) expose-grafana &
	$(MAKE) expose-postgresql &
	minikube dashboard &

end-minikube:
	minikube delete --purge

build-airflow-image:
	docker build -t idp-airflow:latest -t idp-airflow:1.0.0 idp-airflow/

build-spark-image:
	(cd idp-spark; mvn clean package)
	docker build -t idp-spark:latest -t idp-spark:1.0.0 idp-spark/

start-enviroment:
	terraform -chdir=cluster/ init
	terraform -chdir=cluster/ apply

load-data:
	mc alias set idp-tenant http://localhost:9000 access-idp-tenant secret-idp-tenant --insecure
	mc cp --recursive ./configs/ idp-tenant/idp-config/
	mc cp --recursive ./data/raw/ idp-tenant/idp-bronze/raw/

expose-airflow:
	kubectl port-forward svc/airflow-webserver 8080:8080 --namespace airflow-ns

expose-minio:
	kubectl port-forward svc/console 9090:9090 --namespace minio-ns

expose-tenant:
	kubectl port-forward svc/idp-tenant-hl 9000:9000 --namespace tenant-ns

expose-tenant-console:
	kubectl port-forward svc/idp-tenant-console 9091:9090 --namespace tenant-ns

expose-grafana:
	kubectl port-forward $(shell kubectl get pods --namespace grafana-ns -l "app.kubernetes.io/name=grafana,app.kubernetes.io/instance=grafana" -o jsonpath="{.items[0].metadata.name}") 3000:3000 --namespace grafana-ns 
 
expose-postgresql:
	kubectl port-forward --namespace postgresql-ns svc/postgresql 5433:5432

open-psql:
	kubectl run postgresql-postgresql-client --rm --tty -i --restart='Never' --namespace postgresql-ns --image docker.io/bitnami/postgresql:16.1.0-debian-11-r19 --env="PGPASSWORD=$(shell kubectl get secret --namespace postgresql-ns postgresql -o jsonpath="{.data.postgres-password}" | base64 -d))" --command -- psql --host postgresql-postgresql -U postgres -d idp_inmet -p 5432
	

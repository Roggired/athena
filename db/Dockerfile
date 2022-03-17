FROM postgres:latest

ENV POSTGRES_USER='messenger'
ENV POSTGRES_PASSWORD='messenger'
ENV POSTGRES_DATABASE='messenger'

COPY sh/* /docker-entrypoint-initdb.d/
COPY sql/* /scripts/sql/
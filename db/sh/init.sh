set -e

#psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
#    CREATE ROLE messenger WITH ENCRYPTED PASSWORD 'messenger';
#    CREATE DATABASE messenger;
#    GRANT ALL PRIVILEGES ON DATABASE messenger TO messenger;
#EOSQL

psql -v ON_ERROR_STOP=1 messenger messenger <<-EOSQL
    \connect messenger
    \i /scripts/sql/init-messenger-db.sql
EOSQL
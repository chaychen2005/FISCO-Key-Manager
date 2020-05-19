#!/bin/sh


echo -e "try to initialize database"

IP=${1}
PORT=${2}

if [[ ! $IP || ! $PORT ]] ; then
    echo "Usage: sh ${0} ip port"
    echo "eg: sh ${0} 127.0.0.1 8501"
    exit 1
fi

#dbUser
DBUSER="defaultAccount"
#dbPass
PASSWD="defaultPassword"
#dbName
DBNAME="fisco_key_manager"


#connect to database then execute init
cat kms-sql.list | mysql --user=$DBUSER --password=$PASSWD --host=$IP --database=$DBNAME --port=$PORT --default-character-set=utf8;

if [ "$?" == "0" ]; then
    echo -e "initialize successfully\n"
else
    echo -e "initialize fail... \n"
fi

exit

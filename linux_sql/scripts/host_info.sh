#!/bin/bash

# Setup and validate arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# Check the number of arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

# Save machine hostname
hostname=$(hostname -f)

# Retrieve hardware specification variables
lscpu_out=$(lscpu)
cpu_number=$(echo "$lscpu_out" | grep "^CPU(s):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | grep "Architecture" | awk '{print $2}')
cpu_model=$(echo "$lscpu_out" | grep "Model name" | awk -F': ' '{print $2}' | xargs)
cpu_mhz=$(cat /proc/cpuinfo | grep "cpu MHz" | head -1 | awk '{print $4}' | xargs)
l2_cache=$(echo "$lscpu_out" | grep "L2 cache" | awk '{print $3}' | tr -d 'K' | xargs)
total_mem=$(vmstat --unit M | tail -1 | awk '{print $4}' | xargs)
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

# Construct the INSERT statement
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, \"timestamp\") VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp')"

# Export PGPASSWORD environment variable for psql command
export PGPASSWORD=$psql_password

# Insert data into the database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?


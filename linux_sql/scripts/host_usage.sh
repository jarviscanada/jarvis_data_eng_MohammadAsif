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

# Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
diskstats=$(vmstat -d)
df_output=$(df -BM /)

# Retrieve hardware specification variables
memory_free=$(echo "$vmstat_mb" | tail -1 | awk '{print $4}' | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}' | xargs)
disk_io=$(echo "$diskstats" | tail -1 | awk '{print $10}' | xargs)
disk_available=$(echo "$df_output" | tail -1 | awk '{print $4}' | tr -d 'M')
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

# Subquery to find matching id in host_info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')"

# Construct the INSERT statement
insert_stmt="INSERT INTO host_usage(\"timestamp\", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES('$timestamp', $host_id, $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available)"

# Export PGPASSWORD environment variable for psql command
export PGPASSWORD=$psql_password

# Insert data into the database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?


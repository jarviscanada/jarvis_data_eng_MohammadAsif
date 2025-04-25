# Introduction
The Jarvis Linux Cluster Administration (LCA) project monitors and records hardware specifications and resource usage in a multi-node Linux cluster. Designed for the LCA team, it helps manage and optimize performance for future planning. The project uses Bash for scripting, Docker for containerizing PostgreSQL, and Git for version control. It includes two main scripts: host_info.sh for static hardware data and host_usage.sh for real-time CPU and memory monitoring.

# Quick Start
Start a PostgreSQL instance using psql_docker.sh
```bash ./scripts/psql_docker.sh start```
Create tables using ddl.sql
```bash psql -h localhost -p 5432 -d host_agent -U postgres -f sql/ddl.sql ```
Insert hardware specs data into the DB using host_info.sh
```bash ./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "password" ```
Insert hardware usage data into the DB using host_usage.sh
```bash ./scripts/host_usage.sh "localhost" 5432 "host_agent" "postgres" "password" ```
Setup crontab for periodic data collection
```bash crontab -e ```
Add the following line to run host_usage.sh every minute
```bash * * * * * /path/to/scripts/host_usage.sh "localhost" 5432 "host_agent" "postgres" "password" > /tmp/host_usage.log ```


# Implemenation
The project was implemented using: 
- PostgreSQL: data storage 
- Docker: containerize the PostgreSQL database
- Bash Scripts:
	- host_info.sh: Collects and inserts hardware specifications into the database.
	- host_usage.sh: Collects and inserts real-time CPU and memory usage data.
- cron: Automates host_usage.sh to run every minute.
- queries.sql: Provides SQL queries for analyzing collected data.


## Scripts
psql_docker.sh: A script to manage the PostgreSQL Docker container

```bash ./scripts/psql_docker.sh start|stop|create ```
host_info.sh: A script to collect and insert hardware specification data into the PostgreSQL database. ```

```bash ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password ```
host_usage.sh: A script to collect and insert real-time CPU and memory usage data into the PostgreSQL database.

```bash ./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password ```
crontab: Cron job setup to automate the periodic execution of host_usage.sh.

queries.sql: Contains SQL queries to generate reports and analyze the collected data. These queries help resolve business problems such as identifying underutilized servers or planning for capacity upgrades.

## Database Modeling
### `host_info` Table  

| Column Name      | Data Type  | Description          |
|-----------------|-----------|----------------------|
| id             | SERIAL    | Primary key          |
| hostname       | VARCHAR   | Host name            |
| cpu_number     | INTEGER   | CPU count            |
| cpu_architecture | VARCHAR  | CPU architecture     |
| cpu_model      | VARCHAR   | CPU model            |
| cpu_mhz        | REAL      | CPU frequency (MHz)  |
| l2_cache       | INTEGER   | L2 cache size (KB)   |
| total_mem      | INTEGER   | Total memory (MB)    |
| timestamp      | TIMESTAMP | Record time          |

### `host_usage` Table  

| Column Name      | Data Type  | Description          |
|-----------------|-----------|----------------------|
| id             | SERIAL    | Primary key          |
| hostname       | VARCHAR   | Host name            |
| memory_free    | INTEGER   | Free memory (MB)     |
| cpu_idle       | REAL      | CPU idle (%)         |
| cpu_kernel     | REAL      | CPU kernel (%)       |
| disk_io        | INTEGER   | Disk I/O (KB)        |
| disk_available | INTEGER   | Free disk space (MB) |
| timestamp      | TIMESTAMP | Record time          |


# Test
The Bash scripts and SQL DDL were manually tested on a test server. Data integrity was verified by executing the scripts and querying the tables to ensure correct population.

# Deployment
The project was deployed as follows:
- GitHub: Managed source code with Git for version control.
- Docker: Containerized PostgreSQL for consistency.
- Crontab: Automated host_usage.sh to run every minute.

## Steps 
1. Edit Crontab Jobs:
```bash crontab -e. ```
2. Add Entry: 
```bash  * * * * * bash /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent      postgres password > /tmp/host_usage.log```
3. List Crontab Jobs:
```bash crontab -1```
4. Validate Your Results:
```bash psql -h localhost -p 5432 -d host_agent -U postgres ```
``` SELECT * FROM host_usage; ```

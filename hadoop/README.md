Table of contents
* [Introduction](#Introduction)
* [Hadoop Cluster](#Hadoop-Cluster)
* [Hive Project](#Hive-Project)
* [Improvements](#Improvements)

# Introduction
Developed a big data processing solution using Google Cloud Platform (GCP) Dataproc to analyze large datasets efficiently. The project leveraged 
Apache Zeppelin as an interactive data analytics environment and utilized the Hive interpreter for SQL-based querying on distributed data. This 
solution enables scalable data processing and real-time analytics, making it suitable for enterprise-level big data applications.
- Gained experience in managing Hadoop clusters, optimizing SQL queries in Hive, and using Zeppelin for interactive analytics.
- Implemented a Hive-based data processing pipeline on a Dataproc cluster with Zeppelin as the primary interactive interface.

# Hadoop-Cluster
Cluster architecture diagram:
![image](https://github.com/user-attachments/assets/ec0dc1c5-fdef-4d7a-9391-c3220a973c53)
Hardware specifications (3 node hadoop cluster):
- Cluster mode: Standard (1 master, 2 workers)
- Master node:
  - Machine type: 2vCPUs, 13GB memory
  - Primary disk size: 100 GB
  - Primary disk type: Standard persistent disk
- Worker nodes (2):
  - Machine type: 2vCPUs, 13GB memory
  - Primary disk size: 100 GB
  - Primary disk type: Standard persistent disk
- YARN cores: 4
- YARN Memory: 22 GB
- Image version: 2.2.47-debian12

# Hive-Project
- Implemented partitioning and bucketing to improve query execution speed.
- Used ORC file format for efficient storage and columnar access.
- Tuned Hive queries for better performance.
![image](https://github.com/user-attachments/assets/e74d3bf7-5e70-4022-b13e-8440e5e0400b)

# Improvements
- Enhanced Query Performance: Further fine-tune queries with indexing and cost-based optimization.
- Automation: Implement an automated ETL pipeline for data ingestion and transformation.
- Scalability: Explore dynamic cluster scaling for cost-effective resource allocation.

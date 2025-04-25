# Table of contents
* [Introduction](#Introduction)
* [Databrick and Hadoop Implementation](#Databricks-and-Hadoop-Implementation)
* [Zeppelin and Hadoop Implementation](#Zeppelin-and-Hadoop-Implementation)
* [Improvements](#Improvements)

# Introduction
I worked on a proof of concept (PoC) for London Gift Shop, a UK-based online retailer, aimed at 
leveraging data analytics to better understand customer behavior and improve marketing strategies.
The dataset included synthetic customer transaction and engagement information over specific timeframe.
My key responsibilities included:
- Data Analytics & Wrangling: Performed extensive data cleansing, transformation, and analysis
using both PySpark and Scala Spark.
- Tools: Utilized Apache Zeppelin and Databricks notebooks to run and visualize Spark jobs
interactively.

## Technologies:
- PySpark & Scala Spark Structured APIs for scalable data processing.
- Azure Data Lake Storage for data input/output.
- Hadoop HDFS integration for distributed data storage (set up within a dataproc cluster gcp).

# Databricks-and-Hadoop-Implementation
[Spark Notebook](./notebook/PySpark%20Data%20Analytics%20and%20Wrangling.ipynb)
[Scala Notebook](./notebook/Scala%20Data%20Analytics%20and%20Data%20Wrangling.ipynb)
## Dataset and Analytics
This implementation processes retail transaction data using Spark/Scala DataFrames and SQL queries 
within the Databricks ecosystem, enabling ETL operations and generating behavioral visualizations.
The following analytics were conducted:
- Monthly changes (customer growth, orders, sales growth)
- User activity trends
- User classification (RFM segmentation)

## Architecture Details
Storage: Data resides in Azure Blob Storage and is accessed through the Databricks File System (DBFS).
Data Flow: Data is read from DBFS, transformed using Spark’s Structured APIs, and written back to DBFS or Azure Blob Storage.
Hive Metastore: Databricks integrates with the Hive Metastore for data cataloging.

Architecture Diagram:

![image](https://github.com/user-attachments/assets/d595edb4-f85d-49cd-9591-4fa224e67a9e)

# Zeppelin-and-Hadoop-Implementation
Utilized wdi_2016 dataset to run analysis on GDP and other metrics related to countries 

## Architecture Details
Implemented a Hive-based data processing pipeline on a Dataproc cluster with Zeppelin as the 
primary interactive interface. Utilized Spark SQL to run some analytics on the existing 
parquet file related to the previous hadoop project.

Cluster Architecture Diagram:

![image](https://github.com/user-attachments/assets/ec0dc1c5-fdef-4d7a-9391-c3220a973c53)

# Improvements
- Perform CLV Segmentation (Customer Lifetime Value): to predict future revenue from consumers and target.
- Perform A/B Testing: after some target campaigns I would like to gather more data and perform cluster analysis.
- Take a deeper look at the specific classes highlighted in rfm segementation.

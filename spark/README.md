# Table of contents
* [Introduction](#Introduction)
* [Databrick and Hadoop Implementation](#Databricks-and-Hadoop-Implementation)
* [Zeppelin and Hadoop Implementation](#Zeppelin-and-Hadoop-Implementation)
* [Improvements](#Improvements)
* 
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
[Spark Notebook](./notebook/PySpark Data Analytics and Wrangling.ipynb)
[Scala Notebook](./notebook/Scala Data Analytics and Data Wrangling.ipynb)
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

# Zeppelin-and-Hadoop-Implementation
- Describe the dataset and your analytics work (make sure you create a link to your ipynb)
## Architecture Details (e.g. Zeppelin, GCP, Hadoop, Hive Metastore, PySpark, data flow, etc..)

Cluster Architecture Diagram:

![image](https://github.com/user-attachments/assets/ec0dc1c5-fdef-4d7a-9391-c3220a973c53)

# Improvements
- Perform CLV Segmentation (Customer Lifetime Value): to predict future revenue from consumers and target.
- Perform A/B Testing: after some target campaigns I would like to gather more data and perform cluster analysis.
- Take a deeper look at the specific classes highlighted in rfm segementation.

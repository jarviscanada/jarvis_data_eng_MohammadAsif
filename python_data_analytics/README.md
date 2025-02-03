# Introduction
This project is a proof of concept (PoC) for London Gift Shop (LGS), a UK-based online giftware retailer. With 
stagnant revenue growth, LGS seeks to leverage data analytics to understand customer behavior and improve 
marketing strategies. This project delivers insights to support targeted campaigns and boost sales.

# Implementation

## Project Architecture
London Gift Shop's web app runs on a Microsoft Azure resource group, with Azure CDN for the front end and
API Management for the back end. The back end uses a microservice architecture with a scalable AKS cluster
for processing and a single SQL server for OLTP data, from which the proof-of-concept sample data was pulled.
This sample data was stored in a separate PSQL database, with analysis conducted in a Jupyter notebook.

### Architecture Diagram 
![image](https://github.com/user-attachments/assets/6871e9b2-a96c-439a-bd1b-81cd945eade5)


## Data Analytics and Wrangling
[Jupyter Notebook](./retail_data_analytics_wrangling.ipynb)

Within this PoC, the analysis to help LGS address stagnant revenue growth was performed on the following:
- Monthly changes (customer growth, orders, sales growth)
- User activity trends
- User classification (RFM segmentation)

These measures enable LGS to examine specific periods to assess campaign impact and analyze customer segments to understand behavior and optimize targeted advertising.

# Improvements
- Perform CLV Segmentation (Customer Lifetime Value): to predict future revenue from consumers and target.
- Perform A/B Testing: after some target campaigns I would like to gather more data and perform cluster analysis.
- Take a deeper look at the specific classes highlighted in rfm segementation.

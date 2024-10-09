# Introduction

This project was an exploration of SQL (Structured Query Language) and RDBMS (relational Database Management System). To begin a PostgreSQL database was created with Docker and sample data was fed into the cd tables (DDL provided below). The database contains three tables modeling a country club's facilities, bookings, and members. Various queries were performed to dervive meaningful information from the tables shown below.

# SQL Quries

###### Table Setup (DDL)

```sql
CREATE TABLE IF NOT EXISTS cd.members (
    memid INTEGER NOT NULL PRIMARY KEY ,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    address VARCHAR(300) NOT NULL,
    zipcode INTEGER NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    recommendedby INTEGER NOT NULL REFERENCES cd.members(memid) ON DELETE SET NULL,
    joindate TIMESTAMP NOT NULL,
);

CREATE TABLE IF NOT EXISTS cd.bookings (
    bookid INTEGER NOT NULL PRIMARY KEY, 
    facid INTEGER NOT NULL REFERENCES cd.facilities(facid),
    memid INTEGER NOT NULL REFERENCES cd.members(memid),
    starttime TIMESTAMP NOT NULL,
    slots INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS cd.facilities (
    facid INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    membercost NUMERIC NOT NULL,
    guestcost NUMERIC NOT NULL,
    initaloutlay NUMERIC NOT NULL,
    monthlymaintenance NUMERIC NOT NULL
);
```

###### Question 1: insert new facility into database

```sql
insert into cd.facilities
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
values (9, 'Spa', 20, 30, 100000, 800)
```

###### Question 2: insert new facility with auto-generated id into database

```sql
insert into cd.facilities
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
values ((select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800)
```

###### Question 3: Update incorrect initial outlay value

```sql
update cd.facilities
set initialoutlay = 10000
where facid = 1; 
```

###### Question 4: Update value using computed value

```sql
update cd.facilities facs
set
membercost = (select membercost * 1.1 from cd.facilities where facid = 0),
guestcost = (select guestcost * 1.1 from cd.facilities where facid = 0)
where facs.facid = 1;  
```

###### Question 5: Delete all bookings

```sql
delete from cd.bookings
```

###### Question 6: Delete specific member id

```sql
delete from cd.members
where memid = 37;
```

###### Question 7: Select facilities with a membership fee less than 1/50th the monthly maintenance cost

```sql
select facid, name, membercost, monthlymaintenance
from cd.facilities
where membercost > 0 and (membercost < monthlymaintenance/50.0);  
```

###### Question 8: Select all facilities with "Tennis" in their name

```sql
select *
from cd.facilities
where name like '%Tennis%';
```

###### Question 9: Select all facilities with specific id

```sql
select *
from cd.facilities
where facid  = 1 or facid = 5; 
```

###### Question 10: Select all members that join after September 2012

```sql
select memid, surname, firstname, joindate 
from cd.members
where joindate >= '2012-09-01';  
```

###### Question 11: Select all member surnames and facilities names

```sql
select surname 
from cd.members
union
select name
from cd.facilities; 
```

###### Question 12: List start times for bookings made by "David Farrell"

```sql
select bks.starttime 
from cd.bookings bks
inner join cd.members mems
on mems.memid = bks.memid
where mems.firstname='David' and mems.surname='Farrell';
```

###### Question 13: List start times for tennis court bookings on 2012-09-21

```sql
select bks.starttime as start, facs.name as name
from cd.facilities facs
inner join cd.bookings bks
on facs.facid = bks.facid
where 
facs.name in ('Tennis Court 2','Tennis Court 1') and bks.starttime >= '2012-09-21' and bks.starttime < '2012-09-22'
order by bks.starttime;
```

###### Question 14: List all members and who recommended them (if any)

```sql
select mems.firstname as memfname, mems.surname as memsname, recs.firstname as recfname, recs.surname as recsname
from cd.members mems
left outer join cd.members recs
on recs.memid = mems.recommendedby
order by memsname, memfn
```

###### Question 15: List all members who have recommended another member

```sql
select distinct recs.firstname as firstname, recs.surname as surname
from cd.members mems
inner join cd.members recs
on recs.memid = mems.recommendedby
order by surname, firstname;
```

###### Question 16: List all members who have recommended another member (without using JOIN)

```sql
select distinct mems.firstname || ' ' ||  mems.surname as member,
(select recs.firstname || ' ' || recs.surname as recommender 
from cd.members recs 
where recs.memid = mems.recommendedby
)
from cd.members mems
order by mem
```

###### Question 17: Count the number of recommendations each member has made, ordered by member id

```sql
select recommendedby, count(*) 
from cd.members
where recommendedby is not null
group by recommendedby
order by recommendedby; 
```

###### Question 18: Count the total number of slots booked per facility 

```sql
select facid, sum(slots) as "Total Slots"
from cd.bookings
group by facid
order by facid
```

###### Question 19: Count the total number of slots booked per facility on September 2012

```sql
select facid, sum(slots) as "Total Slots"
from cd.bookings
where starttime >= '2012-09-01' and starttime < '2012-10-01'
group by facid
order by sum(slots);
```

###### Question 20: Count the total number of slots booked per facility for each month in 2012

```sql
select facid, extract(month from starttime) as month, sum(slots) as "Total Slots"
from cd.bookings
where extract(year from starttime) = 2012
group by facid, month
order by facid, month;  
```

###### Question 21: List the total number of members and guests that have made at least 1 booking 

```sql
select count(distinct memid)
from cd.bookings;
```

###### Question 22: List each member and their first booking after September 1st 2012 

```sql
select mems.surname, mems.firstname, mems.memid, min(bks.starttime) as starttime
from cd.bookings bks
inner join cd.members mems
on mems.memid = bks.memid
where starttime >= '2012-09-01'
group by mems.surname, mems.firstname, mems.memid
order by mems.memid;
```

###### Question 23: List each member, with each row containing the total member count

```sql
select count(*) over(), firstname, surname
from cd.members
order by joindate  
```

###### Question 24: List each member, number by their date of joining

```sql
select row_number() over(order by joindate), firstname, surname
from cd.members
order by joindate  
```

###### Question 25: Select the facility with the most number of slots booked

```sql
select facid, total from (select facid, sum(slots) total, rank() over (order by sum(slots) desc) rank
from cd.bookings
group by facid)
as ranked
where rank = 1;
```

###### Question 26: List names of all members formatted as "Surname, Firstname"

```sql
select surname || ', ' || firstname as name from cd.members
```

###### Question 27: List all member ids and phone numbers containing parentheses

```sql
select memid, telephone from cd.members where telephone ~ '[()]';
```

###### Question 28: List the count of how many member's surname starts with each letter of the alphabet

```sql
select substr (mems.surname,1,1) as letter, count(*) as count 
from cd.members mems
group by letter
order by letter;
```

EOF

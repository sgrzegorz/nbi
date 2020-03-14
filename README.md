
#Simple Program which uses NBP Web API 

Program written in Java, Jersey. Program connects to NBP Web API gets from it information about currencies. Performs operations on downloaded data. The program can work in two ways:
Version1 on Tomcat server as a browser app  http://localhost:8080/nbi/question. Version2 as a Command Line Application with input argument list.

## Version1 REST application on Tomcat server
Import project to Eclipse. Import Maven dependencies. Run As->Run on Server-> Select server and click Finish. Tested on Tomcat v9.

## Version2 Command Line
Import project to Eclipse. Import Maven dependencies. Set arguments from list of commands in ->Run configuration->Arguments. Run CommandLineParser.java.

##### List of commands

**Get gold price for a specific date [pln/gram]:**
-0 {year} {month} {day}  

**Get currency price for a specific date [pln]**
-1 {code} {year} {month} {day}

**Get average gold price between two dates [pln/gram]**
-2 {year1} {month1} {day1} {year2} {month2} {day2}

**Get most unstable currency in a period of time since a given date until now**
-3 {year} {month} {day}

**Get name of the cheapest currency for given date**
-4 {year} {month} {day}

**Get N currencies, sorted ascending order the price [in pln] of sales and purchase for a given date**
-5 {year} {month} {day} {N} 

**For a given currency get the dates: when it was the cheapest and when most expensive **
-6 {code}

**Print chart for a given currency's price from {week1} week of year to {week2} week of year**
-7 {code} {year1} {month1} {week1} {year2} {month2} {week2} 
# OlderOrdersScala

## Project developed using Intellij Idea, Scala 3.1.0 and JDK 11

### Parameters for the randomly generated Orders are found in Orders.scala as _vals_. By default, 20 orders are generated, with 90% chance of being generated in year 2021.

To compile, it was used:

> ...\ScalaOrders\src\main\scala> scala3-compiler.bat .\Main.scala .\Orders.scala .\entity\*

To run, it was used (for example):
> ...\ScalaOrders\src\main\scala> scala3 com.sales.main "2020-01-01 00:00:00" "2022-01-01 00:00:00"

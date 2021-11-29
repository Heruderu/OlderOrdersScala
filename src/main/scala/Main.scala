package com.sales

import Orders.createRandomOrders
import Orders.countOrders

import java.time.LocalDateTime
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util
import java.util.{HashMap, List}

@main def hello(start: String, end: String) : Unit =
  createRandomOrders()

  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  try
    val startDate = LocalDateTime.parse(start, formatter)
    val endDate = LocalDateTime.parse(end, formatter)

    if (startDate.isEqual(endDate)) {
      println("StartDate and EndDate should be different.")
      println(s"$startDate == $endDate")
      return
    }

    val olderProductsMap: util.HashMap[String, util.List[entity.Product]] = new util.HashMap[String, util.List[entity.Product]]

    countOrders(startDate, endDate, olderProductsMap)

    if (olderProductsMap.isEmpty) {
      println("No older products in this range")
      return
    }

    olderProductsMap.keySet().forEach( key => {
      println(key + " months: " + olderProductsMap.get(key).size)
      olderProductsMap.get(key).forEach( prod => {
        println(prod.name + " - creation date: " + prod.creationDate.format(formatter))
      })
      println()
    })
  catch
    case e: DateTimeParseException => println("Arguments of Date and Time should follow the format: \"yyyy-MM-dd HH:mm:ss\"")
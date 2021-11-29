package com.sales

import entity.{Customer, Item, Order, Product}

import java.time.{LocalDate, LocalDateTime, Period}
import java.time.format.DateTimeFormatter
import java.util
import java.util.{ArrayList, HashMap, List, Random}
import scala.util.control.Breaks.{break, breakable}

object Orders {

  val orders: util.List[Order] = new util.ArrayList[Order]
  var prods: util.List[Product] = new util.ArrayList[Product]
  val numProducts = 20
  val maxNumItems = 5
  val numOrders = 1000
  val minYear = 2018
  val maxYear = 2021
  val percentageOfCurrentYearProducts = 90

  def createRandomOrders(): Unit = {
    val random = new Random
    val productNamesArray = Array[String]("Desktop", "Laptop", "Desktop Gamer", "Mouse", "Keyboard")
    val productCategoriesArray = Array[String]("Computer", "Hardware")
    for (_ <- 0 until numProducts) {
      var year = 0
      var month = 0
      var day = 0

      if (random.nextInt(100) < percentageOfCurrentYearProducts) year = maxYear
      else year = random.nextInt(4) + minYear

      month = if (year == 2021) random.nextInt(11) + 1
      else random.nextInt(12) + 1

      day = if (year == 2021 && month == 11) random.nextInt(27) + 1
      else random.nextInt(28) + 1

      val name = productNamesArray(random.nextInt(productNamesArray.length))
      val category = productCategoriesArray(random.nextInt(productCategoriesArray.length))
      val prod = new Product(
        name,
        category,
        random.nextInt(3),
        random.nextInt(2000),
        LocalDateTime.of(year, month, day,
          random.nextInt(24),
          random.nextInt(60),
          random.nextInt(60)))
      prods.add(prod)
    }

    val customerNamesArray = Array[String]("John", "Mary", "Joseph", "Anna")
    val customerTelephoneArray = Array[String]("999888777", "999777666", "999666555", "999555444")
    val customerAddressArray = Array[String]("Street St", "Avenue Av", "Boulevard Blvd", "Road Rd")
    val customerEmailArray = Array[String]("john@gmail.com", "mary@gmail.com", "joseph@hotmail.com", "anna@sapo.pt")

    for (_ <- 0 until numOrders) {
      val items = new util.ArrayList[Item]
      for (_ <- 0 until random.nextInt(maxNumItems) + 1) {
        val item = new Item(prods.get(random.nextInt(numProducts)))
        items.add(item)
      }

      val customerId = random.nextInt(customerNamesArray.length)
      val customer = new Customer(customerNamesArray(customerId), customerEmailArray(customerId),
        customerTelephoneArray(customerId), customerAddressArray(customerId))

      var datePlaced: LocalDateTime = LocalDateTime.of(1900, 1, 1, //date
        random.nextInt(24), //hour
        random.nextInt(60), //minute
        random.nextInt(60)) //second
      var newestProduct: LocalDateTime = null

      items.forEach(item => {
        if (newestProduct == null || item.product.creationDate.isAfter(newestProduct))
          newestProduct = item.product.creationDate
      })

      while (!datePlaced.isAfter(newestProduct)) {
        var year = 0
        var month = 0
        var day = 0

        if (random.nextInt(100) < percentageOfCurrentYearProducts) year = maxYear
        else year = random.nextInt(4) + minYear

        month = if (year == 2021) random.nextInt(11) + 1
        else random.nextInt(12) + 1

        day = if (year == 2021 && month == 11) random.nextInt(27) + 1
        else random.nextInt(28) + 1

        datePlaced = LocalDateTime.of(year, month, day, //date
          random.nextInt(24), //hour
          random.nextInt(60), //minute
          random.nextInt(60)) //second
      }

      val order = new Order(customer, datePlaced, items)

      orders.add(order)
    }
  }

  def countOrders(startDate: LocalDateTime, endDate: LocalDateTime, olderProductsMap: util.HashMap[String, util.List[Product]]): Unit = {
    orders.forEach(order => {
      if (order.datePlaced.isAfter(startDate) && order.datePlaced.isBefore(endDate)) {
        var oldestProduct: Product = null
        var oldestPeriod: Period = null
        var months: Int = 0

        breakable {
          order.items.forEach(item => {
            val diff = Period.between(LocalDate.now, item.product.creationDate.toLocalDate)
            if (diff.getYears < 0) {
              oldestProduct = item.product
              months = 13
              break
            } else if (diff.getMonths < -6) {
              if (oldestProduct == null) {
                oldestProduct = item.product
                oldestPeriod = diff
                months = oldestPeriod.getMonths * (-1)
                break
              }
              if (diff.getMonths < oldestPeriod.getMonths) {
                oldestProduct = item.product
                oldestPeriod = diff
                months = oldestPeriod.getMonths * (-1)
              }
            } else if (diff.getMonths < -3) {
              if (oldestProduct == null) {
                oldestProduct = item.product
                oldestPeriod = diff
                months = oldestPeriod.getMonths * (-1)
                break

              }
              if (diff.getMonths < oldestPeriod.getMonths) {
                oldestProduct = item.product
                oldestPeriod = diff
                months = oldestPeriod.getMonths * (-1)
              }
            } else if (diff.getDays < 0 || diff.getMonths < 0) {
              oldestProduct = item.product
              oldestPeriod = diff
              months = 1
            } else {
              println("Incorrect Period");
            }
          })
        }
        if (months > 12) {
          if (!olderProductsMap.containsKey(">12")) {
            olderProductsMap.put(">12", new util.ArrayList[Product])
            olderProductsMap.put("7-12", new util.ArrayList[Product])
            olderProductsMap.put("4-6", new util.ArrayList[Product])
            olderProductsMap.put("1-3", new util.ArrayList[Product])
          }
          olderProductsMap.get(">12").add(oldestProduct)
        } else if (months > 6) {
          if (!olderProductsMap.containsKey("7-12")) {
            olderProductsMap.put("7-12", new util.ArrayList[Product]);
            olderProductsMap.put("4-6", new util.ArrayList[Product]);
            olderProductsMap.put("1-3", new util.ArrayList[Product]);
          }
          olderProductsMap.get("7-12").add(oldestProduct);
        } else if (months > 3) {
          if (!olderProductsMap.containsKey("4-6")) {
            olderProductsMap.put("4-6", new util.ArrayList[Product]);
            olderProductsMap.put("1-3", new util.ArrayList[Product]);
          }
          olderProductsMap.get("4-6").add(oldestProduct);
        } else if (months > 0) {
          if (!olderProductsMap.containsKey("1-3"))
            olderProductsMap.put("1-3", new util.ArrayList[Product]);
          olderProductsMap.get("1-3").add(oldestProduct);
        }
      }
    })
  }
}

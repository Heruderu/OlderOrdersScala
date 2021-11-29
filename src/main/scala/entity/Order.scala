package com.sales
package entity

import entity.{Customer, Item}

import java.time.{DateTimeException, LocalDateTime}
import java.util

class Order(var customer: Customer, var datePlaced: LocalDateTime, var items: util.ArrayList[Item]) {

  var grandTotal: Double = 0.0
  items.forEach ( item => {
    if (item.product.creationDate.isAfter(datePlaced))
      throw new DateTimeException("Product creation should not be after order date")
    grandTotal += item.cost + item.taxAmount + item.shippingFee
  })
}

package com.sales
package entity

import entity.Product

class Item(var product: Product) {

  val cost: Double = product.price * 0.9
  val taxAmount: Double = product.price * 0.1
  val shippingFee: Double = 3.99
}


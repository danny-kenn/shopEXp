package com.kiamba.myfirebasemvvm.model

import android.net.Uri

class Product {

    var name: String = ""
    var quantity: String = ""
    var price: String = ""
    var imageUrl: String = "" // This should match your Firebase structure
    var id: String = ""

    constructor(name: String, quantity: String, price: String, id: String) {
        this.name = name
        this.quantity = quantity
        this.price = price
        this.id = id
    }

    constructor() {}
}

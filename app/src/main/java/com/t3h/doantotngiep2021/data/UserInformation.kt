package com.t3h.doantotngiep2021.data

class UserInformation {
    var id: Int = 0
    var name: String = ""
    var age: Int = 0
//    var phone: Int = 0

    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
//        this.phone = phone
    }

    constructor() {
    }
}
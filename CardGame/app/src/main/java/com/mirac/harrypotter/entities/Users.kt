package com.mirac.harrypotter.entities

class Users {
    var userName:String = ""
    var password:String = ""
    var email:String = ""

    constructor(userName: String, password: String, email: String) {
        this.userName = userName
        this.password = password
        this.email = email
    }
}
package com.mason.myapplication

data class UserProfile(var nickname: String, var icon : Int) {
    constructor() : this("", 0)
}
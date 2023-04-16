package com.ktknahmet.final_project.model

data class AddFriendPayment(
    val BUTCE :Double?=0.0,
    val EMAIL :String?="",
    val ARKADAS:String?="",
    val FULLNAME:String?="",
    val ODEMETIP:String?="",
    val PHONE:String?="",
    val TARIH:Long?=0,
    val ODEMETARIH:Long?=0
)
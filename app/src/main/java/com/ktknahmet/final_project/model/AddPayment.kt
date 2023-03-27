package com.ktknahmet.final_project.model

data class AddPayment(
    val BUTCE :Double?=0.0,
    val EMAIL :String?="",
    val FATURATIP:String?="",
    val FULLNAME:String?="",
    val ODEMETIP:String?="",
    val PHONE:String?="",
    val TARIH:Long?=0,
    val ODEMETARIH:Long?=0
)
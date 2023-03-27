package com.ktknahmet.final_project.model

import android.net.Uri

data class UserDeatils(
    val EMAIL :String?="",
    val FULLNAME:String?="",
    val PHONE:String?="",
    val PHOTOURL: Uri? = null,
    val SIFRE:String?=""
)
package com.ktknahmet.final_project.model.loginModel

import com.google.gson.annotations.SerializedName

data class Fields(
    @field:SerializedName("EMAIL")
    val EMALIL: String? = null,

    @field:SerializedName("PHONE")
    val PHONE: String? = null,
    @field:SerializedName("PHOTOURL")
    val PHOTOURL: String? = null,

    @field:SerializedName("SIFRE")
    val SIFRE: String? = null,

    @field:SerializedName("FULLNAME")
    val FULLNAME: String? = null
)
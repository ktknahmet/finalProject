package com.ktknahmet.final_project.model.loginModel

import com.google.gson.annotations.SerializedName

data class LoginItem (

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("fields")
    val fields: Fields? = null,

    @field:SerializedName("createTime")
    val createTime: String? = null,

    @field:SerializedName("updateTime")
    val updateTime: String? = null

)
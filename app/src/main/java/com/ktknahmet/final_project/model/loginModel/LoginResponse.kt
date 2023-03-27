package com.ktknahmet.final_project.model.loginModel

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("documents")
    val documents: List<LoginItem?>? = null
)
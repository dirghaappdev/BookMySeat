package com.dirgha.bookmyseat.data.model

data class Profile(

    val userId: String,

    val name: String,

    val phoneNo: String,

    val email: String,

    val role: String

)

data class UpdateProfileRequest(

    val name: String,

//    val phoneNo: String,
      val email: String

)
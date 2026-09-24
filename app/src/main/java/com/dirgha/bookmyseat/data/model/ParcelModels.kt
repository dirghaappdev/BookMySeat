package com.dirgha.bookmyseat.data.model

data class WeightCategory(
    val weightCategoryId: String,
    val categoryName: String,
    val minWeight: Double,
    val maxWeight: Double?,
    val isActive: Boolean
)

data class ParcelFare(
    val parcelFareId: String,
    val routeId: String,
    val direction: String,

    val route: String,
    val routeName: String,

    val weightCategoryId: String,
    val categoryName: String,

    val minWeight: Double,
    val maxWeight: Double?,

    val minFare: Double,
    val maxFare: Double,

    val isActive: Boolean
)

data class CreateParcelRequest(
    val parcelFareId: String?,
    val weightCategoryId: String,
    val expectedDate: String,
    val parcelType: String,
    val weight: Double,
    val contactPersonName: String,
    val contactPersonPhone: String,
    val note: String = ""
)

data class CreateParcelResponse(
    val parcelId: String,
    val parcelStatus: String,
    val message: String
)

data class Parcel(
    val parcelId: String,

    val parcelType: String,
    val weight: Double,

    val categoryName: String,

    val expectedDate: String,
    val actualDate: String?,
    val actualTime: String?,

    val routeId: String?,
    val direction: String?,
    val route: String?,

    val tripId: String?,

    val agreedFare: Double?,

    val parcelStatus: String,

    val rejectionReason: String?,

    // Added because admin screen uses these
    val userName: String = "",
    val mobileNumber: String = "",
    val note: String = "",

    val contactPersonName: String = "",
    val contactPersonPhone: String = "",
)

data class ParcelBookingData(
    val parcelFareId: String? = null,
    val weightCategoryId: String = "",
    val categoryName: String = "",
    val routeId: String = "",
    val route: String = "",
    val direction: String = "",
    val parcelType: String = "",
    val weight: Double = 0.0,

    val contactPersonName: String = "",
    val contactPersonPhone: String = "",

    val expectedDate: String = "",
    val note: String = "",
    val minFare: Double = 0.0,
    val maxFare: Double = 0.0
)

data class ParcelAllocateRequest(
    val tripId: String,
    val agreedFare: Double
)

data class ParcelRejectRequest(
    val reason: String
)

data class ParcelActionResponse(
    val message: String,
    val parcelId: String? = null,
    val tripId: String? = null,
    val actualDate: String? = null,
    val actualTime: String? = null,
    val agreedFare: Double? = null,
    val parcelStatus: String? = null
)

data class ParcelRescheduleRequest(

    val tripId: String,

    val agreedFare: Double
)
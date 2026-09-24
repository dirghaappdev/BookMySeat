package com.dirgha.bookmyseat.ui.model

data class AdminAnalyticsResponse(

    val period: String? = null,
    val year: Int? = null,
    val month: Int? = null,

    val startDate: String? = null,
    val endDate: String? = null,

    // Business
    val totalTrips: Int = 0,
    val previousTrips: Int = 0,

    val totalBookings: Int = 0,
    val confirmedBookings: Int = 0,
    val pendingBookings: Int = 0,
    val rejectedBookings: Int = 0,

    val totalRevenue: Double = 0.0,
    val averageBookingValue: Double = 0.0,

    val revenueChange: Double = 0.0,
    val bookingsChange: Double = 0.0,

    // Ride
    val rideBookings: Int = 0,
    val confirmedRideBookings: Int = 0,
    val rideRevenue: Double = 0.0,
    val averageRideFare: Double = 0.0,

    val totalPassengers: Int = 0,
    val confirmedPassengers: Int = 0,

    // Seats
    val totalSeats: Int = 0,
    val bookedSeats: Int = 0,
    val availableSeats: Int = 0,
    val overbookedSeats: Int = 0,
    val seatUtilization: Double = 0.0,

    // Parcel
    val parcelBookings: Int = 0,
    val confirmedParcels: Int = 0,
    val deliveredParcels: Int = 0,
    val pendingParcels: Int = 0,
    val rejectedParcels: Int = 0,

    val parcelRevenue: Double = 0.0,
    val totalParcels: Int = 0,
    val totalParcelWeight: Double = 0.0,
    val confirmedParcelWeight: Double = 0.0,
    val averageParcelFare: Double = 0.0,

    // Business mix
    val businessMix: BusinessMix = BusinessMix(),

    // Routes
    val mostTravelledRoute: RouteAnalytics? = null,
    val leastTravelledRoute: RouteAnalytics? = null,
    val routeAnalytics: List<RouteAnalytics> = emptyList(),

    // Parcel
    val mostBookedParcelType: ParcelTypeAnalytics? = null,
    val parcelTypeAnalytics: List<ParcelTypeAnalytics> = emptyList(),
    val parcelWeightAnalytics: List<ParcelWeightAnalytics> = emptyList(),

    // Graph
    val revenueTrend: List<RevenueTrend> = emptyList(),

    // Recent
    val recentBookings: List<RecentActivity> = emptyList()
)


data class BusinessMix(

    val rideBookings: Int = 0,
    val parcelBookings: Int = 0,

    val confirmedRideBookings: Int = 0,
    val confirmedParcels: Int = 0,

    val rideRevenue: Double = 0.0,
    val parcelRevenue: Double = 0.0,

    val totalRevenue: Double = 0.0
)


data class RouteAnalytics(

    val route: String = "",

    val bookings: Int = 0,
    val rideBookings: Int = 0,
    val parcelBookings: Int = 0,

    val passengers: Int = 0,
    val parcels: Int = 0,

    val travelUnits: Int = 0,

    val weight: Double = 0.0,

    val revenue: Double = 0.0,
    val rideRevenue: Double = 0.0,
    val parcelRevenue: Double = 0.0
)


data class ParcelTypeAnalytics(

    val parcelType: String = "",

    val bookings: Int = 0,
    val parcels: Int = 0,

    val weight: Double = 0.0,

    val revenue: Double = 0.0
)


data class ParcelWeightAnalytics(

    val category: String = "",

    val parcels: Int = 0,

    val weight: Double = 0.0,

    val revenue: Double = 0.0
)


data class RevenueTrend(

    val date: String = "",
    val label: String = "",

    val revenue: Double = 0.0,
    val rideRevenue: Double = 0.0,
    val parcelRevenue: Double = 0.0,

    val bookings: Int = 0,
    val rideBookings: Int = 0,
    val parcelBookings: Int = 0,

    val passengers: Int = 0,
    val parcels: Int = 0,

    val parcelWeight: Double = 0.0
)


data class RecentActivity(

    val id: String = "",
    val bookingId: String = "",

    val route: String = "",
    val date: String = "",
    val timeSlot: String = "",

    val bookingType: String = "RIDE",

    val quantity: Int = 0,
    val passengers: Int = 0,

    val parcelType: String = "",
    val parcelWeight: Double = 0.0,

    val totalFare: Double = 0.0,

    val status: String = ""
)
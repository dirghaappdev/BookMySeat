package com.dirgha.bookmyseat.data.remote


import com.dirgha.bookmyseat.data.model.Booking
import com.dirgha.bookmyseat.data.model.BookingResponse
import com.dirgha.bookmyseat.data.model.CreateBookingRequest
import com.dirgha.bookmyseat.data.model.CreateTripRequest
import com.dirgha.bookmyseat.data.model.CreateTripResponse
import com.dirgha.bookmyseat.data.model.LoginRequest
import com.dirgha.bookmyseat.data.model.LoginResponse
import com.dirgha.bookmyseat.data.model.RegisterRequest
import com.dirgha.bookmyseat.data.model.Route
import com.dirgha.bookmyseat.data.model.SearchTripRequest
import com.dirgha.bookmyseat.data.model.Trip
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Map<String, String>>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/admin/trips")
    suspend fun createTrip(
        @Header("Authorization")
        token: String,

        @Body
        request: CreateTripRequest
    ): Response<CreateTripResponse>

    @GET("api/admin/bookings")
    suspend fun getAllBookings(
        @Header("Authorization")
        token: String
    ): Response<List<Booking>>

    @PUT("api/admin/bookings/{bookingId}/confirm")
    suspend fun confirmBooking(
        @Header("Authorization")
        token: String,

        @Path("bookingId")
        bookingId: String
    ): Response<Map<String, String>>

    @PUT("api/admin/bookings/{bookingId}/reject")
    suspend fun rejectBooking(
        @Header("Authorization")
        token: String,

        @Path("bookingId")
        bookingId: String
    ): Response<Map<String, String>>

    @POST("api/trips/search")
    suspend fun searchTrips(
        @Body request: SearchTripRequest
    ): Response<List<Trip>>

    @GET("api/routes")
    suspend fun getRoutes():
            Response<List<Route>>

    @POST("api/bookings")
    suspend fun createBooking(
        @Header("Authorization")
        token: String,

        @Body
        request: CreateBookingRequest
    ): Response<BookingResponse>

    @GET("api/bookings/my")
    suspend fun getMyBookings(
        @Header("Authorization")
        token: String
    ): Response<List<Booking>>

    @GET("api/trips")
    suspend fun getAllTrips():
            Response<List<Trip>>
}

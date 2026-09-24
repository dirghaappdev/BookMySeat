package com.dirgha.bookmyseat.data.remote


import com.dirgha.bookmyseat.data.model.Parcel
import com.dirgha.bookmyseat.data.config.ConfigResponse
import com.dirgha.bookmyseat.data.model.Booking
import com.dirgha.bookmyseat.data.model.BookingResponse
import com.dirgha.bookmyseat.data.model.ChangePasswordRequest
import com.dirgha.bookmyseat.data.model.CreateBookingRequest
import com.dirgha.bookmyseat.data.model.CreateTripRequest
import com.dirgha.bookmyseat.data.model.CreateTripResponse
import com.dirgha.bookmyseat.data.model.FCMTokenRequest
import com.dirgha.bookmyseat.data.model.ForgotPasswordRequest
import com.dirgha.bookmyseat.data.model.LoginRequest
import com.dirgha.bookmyseat.data.model.LoginResponse
import com.dirgha.bookmyseat.data.model.MessageResponse
import com.dirgha.bookmyseat.data.model.Notification
import com.dirgha.bookmyseat.data.model.RegisterRequest
import com.dirgha.bookmyseat.data.model.RejectBookingRequest
import com.dirgha.bookmyseat.data.model.Route
import com.dirgha.bookmyseat.data.model.SearchTripRequest
import com.dirgha.bookmyseat.data.model.Trip
import retrofit2.Response
import retrofit2.http.*
import com.dirgha.bookmyseat.data.model.Profile
import com.dirgha.bookmyseat.data.model.UpdateProfileRequest
import com.dirgha.bookmyseat.data.model.RegisterOtpRequest
import com.dirgha.bookmyseat.data.model.RegisterVerifyRequest
import com.dirgha.bookmyseat.data.model.ResetPasswordRequest
import com.dirgha.bookmyseat.data.model.VerifyOtpRequest
import com.dirgha.bookmyseat.data.model.CreateParcelRequest
import com.dirgha.bookmyseat.data.model.CreateParcelResponse
import com.dirgha.bookmyseat.data.model.ParcelActionResponse
import com.dirgha.bookmyseat.data.model.ParcelAllocateRequest
import com.dirgha.bookmyseat.data.model.ParcelFare
import com.dirgha.bookmyseat.data.model.ParcelRejectRequest
import com.dirgha.bookmyseat.data.model.ParcelRescheduleRequest
import com.dirgha.bookmyseat.data.model.WeightCategory
import com.dirgha.bookmyseat.ui.model.AdminAnalyticsResponse

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
        bookingId: String,
        @Body reason: RejectBookingRequest
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

    @POST("api/users/fcm-token")
    suspend fun saveFcmToken(

        @Header("Authorization")
        token:String,

        @Body
        request: FCMTokenRequest

    ): Response<MessageResponse>

    @GET("api/notifications")
    suspend fun getNotifications(): Response<List<Notification>>

    @PUT("api/notifications/{notification_id}/read")
    suspend fun markNotificationRead(
        @Path("notification_id")
        notificationId: String
    ): Response<MessageResponse>

    @PUT("api/notifications/read-all")
    suspend fun markAllNotificationsRead(): Response<MessageResponse>

    @GET("api/trips")
    suspend fun getAllTrips():
            Response<List<Trip>>

    //================ Profile ===================

    @GET("api/users/profile")
    suspend fun getProfile(): Response<Profile>

    @PUT("api/users/profile")
    suspend fun updateProfile(
      //  @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<MessageResponse>

    @DELETE("api/users/profile")
    suspend fun deleteProfile(): Response<MessageResponse>

    @PUT("api/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<MessageResponse>

    @POST("api/auth/register/send-otp")
    suspend fun registerSendOtp(
        @Body request: RegisterOtpRequest
    ): Response<MessageResponse>
    @POST("api/auth/verify-otp")
    suspend fun verifyForgotPasswordOtp(

        @Body request: VerifyOtpRequest

    ): Response<MessageResponse>

    @POST("api/auth/register/verify")
    suspend fun registerVerifyOtp(
        @Body request: RegisterVerifyRequest
    ): Response<MessageResponse>

    @PUT("api/auth/reset-password")
    suspend fun resetPassword(

        @Body request: ResetPasswordRequest

    ): Response<MessageResponse>

    @POST("api/auth/send-otp")
    suspend fun sendForgotPasswordOtp(

        @Body request: ForgotPasswordRequest

    ): Response<MessageResponse>
    @GET("api/config")
    suspend fun getConfiguration(): Response<ConfigResponse>

    // ============================================================
// PARCEL
// ============================================================

    @GET("api/parcels/weight-categories")
    suspend fun getWeightCategories():
            Response<List<WeightCategory>>

    @GET("api/parcels/fares/route/{routeId}/{direction}")
    suspend fun getParcelFares(
        @Path("routeId") routeId: String,
        @Path("direction") direction: String
    ): Response<List<ParcelFare>>

    @POST("api/parcels")
    suspend fun createParcel(
        @Body request: CreateParcelRequest
    ): Response<CreateParcelResponse>

    @GET("api/parcels/my")
    suspend fun getMyParcels(): Response<List<Parcel>>


    // ============================================================
// PARCEL - ADMIN
// ============================================================

    @GET("api/parcels/admin/all")
    suspend fun getAllParcels():
            Response<List<Parcel>>

    @PUT("api/parcels/admin/{parcel_id}/allocate")
    suspend fun allocateParcel(
        @Path("parcel_id") parcelId: String,
        @Body request: ParcelAllocateRequest
    ): Response<ParcelActionResponse>

    @PUT("api/parcels/admin/{parcel_id}/reject")
    suspend fun rejectParcel(
        @Path("parcel_id") parcelId: String,
        @Body request: ParcelRejectRequest
    ): Response<ParcelActionResponse>

    @PUT("api/parcels/admin/{parcel_id}/delivered")
    suspend fun markParcelDelivered(
        @Path("parcel_id") parcelId: String
    ): Response<ParcelActionResponse>

    @PUT("api/parcels/admin/{parcelId}/reschedule")
    suspend fun rescheduleParcel(
        @Path("parcelId") parcelId: String,
        @Body request: ParcelRescheduleRequest
    ): Response<CreateParcelResponse>


    @GET("api/admin/analytics/summary")
    suspend fun getAdminAnalytics(
        @Query("period") period: String,
        @Query("year") year: Int? = null,
        @Query("month") month: Int? = null
    ): AdminAnalyticsResponse
}

package com.dirgha.bookmyseat.repository

import com.dirgha.bookmyseat.data.model.CreateBookingRequest
import com.dirgha.bookmyseat.data.remote.RetrofitClient

class BookingRepository {

    suspend fun createBooking(
        token: String,
        request: CreateBookingRequest
    ) =
        RetrofitClient.api.createBooking(
            token,
            request
        )

    suspend fun getAllBookings(
        token: String
    ) =
        RetrofitClient.api.getAllBookings(
            token
        )

    suspend fun confirmBooking(
        token: String,
        bookingId: String
    ) =
        RetrofitClient.api.confirmBooking(
            token,
            bookingId
        )

    suspend fun getMyBookings(
        token: String
    ) =
        RetrofitClient.api.getMyBookings(
            token
        )
    suspend fun rejectBooking(
        token: String,
        bookingId: String
    ) =
        RetrofitClient.api.rejectBooking(
            token,
            bookingId
        )
}
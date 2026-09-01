package com.dirgha.bookmyseat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.dirgha.bookmyseat.data.model.CreateParcelRequest
import com.dirgha.bookmyseat.data.model.CreateParcelResponse
import com.dirgha.bookmyseat.data.model.Parcel
import com.dirgha.bookmyseat.data.model.ParcelActionResponse
import com.dirgha.bookmyseat.data.model.ParcelAllocateRequest
import com.dirgha.bookmyseat.data.model.ParcelFare
import com.dirgha.bookmyseat.data.model.ParcelRejectRequest
import com.dirgha.bookmyseat.data.model.ParcelRescheduleRequest
import com.dirgha.bookmyseat.data.model.Route
import com.dirgha.bookmyseat.data.model.Trip
import com.dirgha.bookmyseat.data.model.WeightCategory
import com.dirgha.bookmyseat.data.remote.RetrofitClient

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParcelViewModel : ViewModel() {

    // ============================================================
    // ROUTES
    // ============================================================

    private val _routes =
        MutableStateFlow<List<Route>>(emptyList())

    val routes: StateFlow<List<Route>> =
        _routes.asStateFlow()


    // ============================================================
    // WEIGHT CATEGORIES
    // ============================================================

    private val _weightCategories =
        MutableStateFlow<List<WeightCategory>>(emptyList())

    val weightCategories: StateFlow<List<WeightCategory>> =
        _weightCategories.asStateFlow()


    // ============================================================
    // PARCEL FARES
    // ============================================================

    private val _parcelFares =
        MutableStateFlow<List<ParcelFare>>(emptyList())

    val parcelFares: StateFlow<List<ParcelFare>> =
        _parcelFares.asStateFlow()


    // ============================================================
    // MY PARCELS
    // ============================================================

    private val _parcels =
        MutableStateFlow<List<Parcel>>(emptyList())

    val parcels: StateFlow<List<Parcel>> =
        _parcels.asStateFlow()


    // ============================================================
    // ADMIN PARCELS
    // ============================================================

    private val _adminParcels =
        MutableStateFlow<List<Parcel>>(emptyList())

    val adminParcels: StateFlow<List<Parcel>> =
        _adminParcels.asStateFlow()


    // ============================================================
    // TRIPS
    // ============================================================

    private val _trips =
        MutableStateFlow<List<Trip>>(emptyList())

    val trips: StateFlow<List<Trip>> =
        _trips.asStateFlow()


    // ============================================================
    // LOADING
    // ============================================================

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()


    // ============================================================
    // ACTION LOADING
    // ============================================================

    private val _isActionLoading =
        MutableStateFlow(false)

    val isActionLoading: StateFlow<Boolean> =
        _isActionLoading.asStateFlow()


    // ============================================================
    // ERROR
    // ============================================================

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()


    // ============================================================
    // ACTION MESSAGE
    // ============================================================

    private val _actionMessage =
        MutableStateFlow<String?>(null)

    val actionMessage: StateFlow<String?> =
        _actionMessage.asStateFlow()


    // ============================================================
    // CREATE PARCEL RESPONSE
    // ============================================================

    private val _bookingResponse =
        MutableStateFlow<CreateParcelResponse?>(null)

    val bookingResponse:
            StateFlow<CreateParcelResponse?> =
        _bookingResponse.asStateFlow()


    // ============================================================
    // ADMIN ACTION RESPONSE
    // ============================================================

    private val _adminActionResponse =
        MutableStateFlow<ParcelActionResponse?>(null)

    val adminActionResponse:
            StateFlow<ParcelActionResponse?> =
        _adminActionResponse.asStateFlow()


    // ============================================================
    // PARCEL DRAFT
    // ============================================================

    private val _selectedRoute =
        MutableStateFlow<Route?>(null)

    val selectedRoute:
            StateFlow<Route?> =
        _selectedRoute.asStateFlow()


    private val _selectedDirection =
        MutableStateFlow("")

    val selectedDirection:
            StateFlow<String> =
        _selectedDirection.asStateFlow()


    private val _parcelType =
        MutableStateFlow("")

    val parcelType:
            StateFlow<String> =
        _parcelType.asStateFlow()


    private val _selectedCategoryId =
        MutableStateFlow("")

    val selectedCategoryId:
            StateFlow<String> =
        _selectedCategoryId.asStateFlow()


    private val _expectedDate =
        MutableStateFlow("")

    val expectedDate:
            StateFlow<String> =
        _expectedDate.asStateFlow()


    private val _note =
        MutableStateFlow("")

    val note:
            StateFlow<String> =
        _note.asStateFlow()


    private val _selectedFare =
        MutableStateFlow<ParcelFare?>(null)

    val selectedFare:
            StateFlow<ParcelFare?> =
        _selectedFare.asStateFlow()

    private val _contactPersonName =
        MutableStateFlow("")

    val contactPersonName:
            StateFlow<String> =
        _contactPersonName.asStateFlow()


    private val _contactPersonPhone =
        MutableStateFlow("")

    val contactPersonPhone:
            StateFlow<String> =
        _contactPersonPhone.asStateFlow()


    // ============================================================
    // LOAD ROUTES
    // ============================================================

    fun loadRoutes() {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                val response =
                    RetrofitClient.api.getRoutes()

                if (response.isSuccessful) {

                    _routes.value =
                        response.body()
                            ?.filter { it.active }
                            ?: emptyList()

                } else {

                    _error.value =
                        "Unable to load routes"
                }

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Unable to load routes"

            } finally {

                _isLoading.value = false
            }
        }
    }


    // ============================================================
    // LOAD WEIGHT CATEGORIES
    // ============================================================

    fun loadWeightCategories() {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                val response =
                    RetrofitClient.api
                        .getWeightCategories()

                if (response.isSuccessful) {

                    _weightCategories.value =
                        response.body()
                            ?.filter { it.isActive }
                            ?: emptyList()

                } else {

                    _error.value =
                        "Unable to load weight categories"
                }

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Unable to load weight categories"

            } finally {

                _isLoading.value = false
            }
        }
    }


    // ============================================================
    // LOAD PARCEL FARES
    // ============================================================

    fun loadParcelFares(
        routeId: String,
        direction: String
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                _parcelFares.value =
                    emptyList()

                val response =
                    RetrofitClient.api
                        .getParcelFares(
                            routeId = routeId,
                            direction = direction
                        )

                if (response.isSuccessful) {

                    _parcelFares.value =
                        response.body()
                            ?.filter { it.isActive }
                            ?: emptyList()

                } else {

                    _error.value =
                        "Unable to load parcel fares"
                }

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Unable to load parcel fares"

            } finally {

                _isLoading.value = false
            }
        }
    }


    // ============================================================
    // CREATE PARCEL
    // ============================================================

    fun createParcel() {

        val fare =
            _selectedFare.value

        val currentDate =
            _expectedDate.value

        val currentParcelType =
            _parcelType.value

        val currentCategoryId =
            _selectedCategoryId.value

        if (fare == null) {

            _error.value =
                "Parcel fare is not available"

            return
        }

        if (currentDate.isBlank()) {

            _error.value =
                "Expected date is required"

            return
        }

        if (currentParcelType.isBlank()) {

            _error.value =
                "Parcel type is required"

            return
        }

        if (currentCategoryId.isBlank()) {

            _error.value =
                "Weight category is required"

            return
        }

        val selectedCategory =
            _weightCategories.value.find {

                it.weightCategoryId ==
                        currentCategoryId
            }

        if (selectedCategory == null) {

            _error.value =
                "Selected weight category is not available"

            return
        }

        val minimumWeight =
            selectedCategory.minWeight

        if (minimumWeight < 0) {

            _error.value =
                "Invalid minimum weight"

            return
        }

        val request =
            CreateParcelRequest(

                parcelFareId =
                    fare.parcelFareId,

                weightCategoryId =
                    selectedCategory.weightCategoryId,

                expectedDate =
                    currentDate,

                parcelType =
                    currentParcelType,

                weight =
                    minimumWeight,

                note =
                    _note.value,
                contactPersonName =
                    _contactPersonName.value,

                contactPersonPhone =
                    _contactPersonPhone.value,
            )

        createParcelApi(request)
    }


    // ============================================================
    // CREATE PARCEL API
    // ============================================================

    private fun createParcelApi(
        request: CreateParcelRequest
    ) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null
                _bookingResponse.value = null

                val response =
                    RetrofitClient.api
                        .createParcel(request)

                if (response.isSuccessful) {

                    _bookingResponse.value =
                        response.body()

                } else {

                    _error.value =
                        "Unable to create parcel request"
                }

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Something went wrong"

            } finally {

                _isLoading.value = false
            }
        }
    }


    // ============================================================
    // MY PARCELS
    // ============================================================

    fun loadMyParcels() {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                val response =
                    RetrofitClient.api
                        .getMyParcels()

                if (response.isSuccessful) {

                    _parcels.value =
                        response.body()
                            ?: emptyList()

                } else {

                    _parcels.value =
                        emptyList()

                    _error.value =
                        "Unable to load parcels"
                }

            } catch (e: Exception) {

                _parcels.value =
                    emptyList()

                _error.value =
                    e.message
                        ?: "Unable to load parcels"

            } finally {

                _isLoading.value = false
            }
        }
    }


    // ============================================================
// ADMIN - LOAD ALL PARCELS
// ============================================================

    fun loadAdminParcels() {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                val response =
                    RetrofitClient.api.getAllParcels()

                if (response.isSuccessful) {

                    _adminParcels.value =
                        response.body() ?: emptyList()

                } else {

                    _adminParcels.value =
                        emptyList()

                    _error.value =
                        "Unable to load parcel bookings"
                }

            } catch (e: Exception) {

                _adminParcels.value =
                    emptyList()

                _error.value =
                    "Unable to load parcel bookings"

            } finally {

                _isLoading.value = false
            }
        }
    }


// ============================================================
// ADMIN - LOAD TRIPS
// ============================================================

    fun loadTrips() {

        viewModelScope.launch {

            try {

                val response =
                    RetrofitClient.api.getAllTrips()

                if (response.isSuccessful) {

                    _trips.value =
                        response.body() ?: emptyList()

                } else {

                    _error.value =
                        "Unable to load trips"
                }

            } catch (e: Exception) {

                _error.value =
                    "Unable to load trips"
            }
        }
    }


// ============================================================
// ADMIN - ALLOCATE PARCEL
// ============================================================

    fun allocateParcel(
        parcelId: String,
        tripId: String,
        agreedFare: Double
    ) {

        if (tripId.isBlank()) {

            _error.value =
                "Please select a trip"

            return
        }

        if (agreedFare <= 0) {

            _error.value =
                "Please enter a valid fare"

            return
        }

        viewModelScope.launch {

            try {

                _isActionLoading.value = true

                _error.value = null
                _actionMessage.value = null

                val request =
                    ParcelAllocateRequest(
                        tripId = tripId,
                        agreedFare = agreedFare
                    )

                val response =
                    RetrofitClient.api.allocateParcel(
                        parcelId = parcelId,
                        request = request
                    )

                if (response.isSuccessful) {

                    val result =
                        response.body()

                    _adminActionResponse.value =
                        result

                    _actionMessage.value =
                        result?.message
                            ?: "Parcel confirmed successfully"

                    loadAdminParcels()

                } else {

                    _error.value =
                        "Unable to confirm parcel"
                }

            } catch (e: Exception) {

                _error.value =
                    "Unable to confirm parcel"

            } finally {

                _isActionLoading.value = false
            }
        }
    }


// ============================================================
// ADMIN - REJECT PARCEL
// ============================================================

    fun rejectParcel(
        parcelId: String,
        reason: String
    ) {

        if (reason.isBlank()) {

            _error.value =
                "Please enter rejection reason"

            return
        }

        viewModelScope.launch {

            try {

                _isActionLoading.value = true

                _error.value = null
                _actionMessage.value = null

                val request =
                    ParcelRejectRequest(
                        reason = reason.trim()
                    )

                val response =
                    RetrofitClient.api.rejectParcel(
                        parcelId = parcelId,
                        request = request
                    )

                if (response.isSuccessful) {

                    val result =
                        response.body()

                    _adminActionResponse.value =
                        result

                    _actionMessage.value =
                        result?.message
                            ?: "Parcel rejected successfully"

                    loadAdminParcels()

                } else {

                    _error.value =
                        "Unable to reject parcel"
                }

            } catch (e: Exception) {

                _error.value =
                    "Unable to reject parcel"

            } finally {

                _isActionLoading.value = false
            }
        }
    }


// ============================================================
// ADMIN - MARK DELIVERED
// ============================================================

    fun markParcelDelivered(
        parcelId: String
    ) {

        viewModelScope.launch {

            try {

                _isActionLoading.value = true

                _error.value = null
                _actionMessage.value = null

                val response =
                    RetrofitClient.api
                        .markParcelDelivered(parcelId)

                if (response.isSuccessful) {

                    val result =
                        response.body()

                    _adminActionResponse.value =
                        result

                    _actionMessage.value =
                        result?.message
                            ?: "Parcel marked as delivered"

                    loadAdminParcels()

                } else {

                    _error.value =
                        "Unable to mark parcel delivered"
                }

            } catch (e: Exception) {

                _error.value =
                    "Unable to mark parcel delivered"

            } finally {

                _isActionLoading.value = false
            }
        }
    }


    // ============================================================
    // SAVE PARCEL DRAFT
    // ============================================================

    fun saveParcelDraft(
        route: Route,
        direction: String,
        parcelType: String,
        categoryId: String,
        expectedDate: String,
        note: String,
        fare: ParcelFare,
        contactPersonName: String,
        contactPersonPhone: String
    ) {

        _selectedRoute.value =
            route

        _selectedDirection.value =
            direction

        _parcelType.value =
            parcelType

        _selectedCategoryId.value =
            categoryId

        _expectedDate.value =
            expectedDate

        _note.value =
            note

        _selectedFare.value =
            fare

        _contactPersonName.value =
            contactPersonName

        _contactPersonPhone.value =
            contactPersonPhone
    }


    // ============================================================
    // SET PARCEL SELECTION
    // ============================================================

    fun setParcelSelection(
        route: Route,
        direction: String,
        parcelType: String,
        categoryId: String,
        expectedDate: String,
        fare: ParcelFare,
        contactPersonName: String,
        contactPersonPhone: String
    ) {

        _selectedRoute.value =
            route

        _selectedDirection.value =
            direction

        _parcelType.value =
            parcelType

        _selectedCategoryId.value =
            categoryId

        _expectedDate.value =
            expectedDate

        _selectedFare.value =
            fare

        _contactPersonName.value =
            contactPersonName

        _contactPersonPhone.value =
            contactPersonPhone
    }


    // ============================================================
    // CLEAR ERROR
    // ============================================================

    fun clearError() {

        _error.value = null
    }


    // ============================================================
    // CLEAR ACTION MESSAGE
    // ============================================================

    fun clearActionMessage() {

        _actionMessage.value = null
    }


    // ============================================================
    // CLEAR BOOKING RESPONSE
    // ============================================================

    fun clearBookingResponse() {

        _bookingResponse.value = null
    }


    // ============================================================
    // CLEAR PARCEL DRAFT
    // ============================================================

    fun clearParcelDraft() {

        _selectedRoute.value = null

        _selectedDirection.value = ""

        _parcelType.value = ""

        _selectedCategoryId.value = ""

        _expectedDate.value = ""

        _note.value = ""

        _selectedFare.value = null

        _bookingResponse.value = null

        _error.value = null
    }
    fun rescheduleParcel(
        parcelId: String,
        tripId: String,
        agreedFare: Double
    ) {

        viewModelScope.launch {

            try {

                _isActionLoading.value = true

                val request =
                    ParcelRescheduleRequest(
                        tripId = tripId,
                        agreedFare = agreedFare
                    )

                val response =
                    RetrofitClient.api.rescheduleParcel(
                        parcelId,
                        request
                    )

                if (response.isSuccessful) {

                    _actionMessage.value =
                        response.body()?.message
                            ?: "Parcel rescheduled successfully"

                    loadAdminParcels()

                } else {

                    _error.value =
                        "Failed to reschedule parcel"
                }

            } catch (e: Exception) {

                _error.value =
                    e.message
                        ?: "Something went wrong"

            } finally {

                _isActionLoading.value = false
            }
        }
    }
}
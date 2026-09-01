package com.dirgha.bookmyseat.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dirgha.bookmyseat.data.model.Route
import com.dirgha.bookmyseat.ui.viewmodel.ParcelViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelBookingScreen(
    parcelViewModel: ParcelViewModel,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {

    // ============================================================
    // LOCAL STATE
    // ============================================================

    var selectedRoute by remember {
        mutableStateOf<Route?>(null)
    }

    var selectedDirection by remember {
        mutableStateOf("")
    }

    var selectedSubRoute by remember {
        mutableStateOf("")
    }

    var parcelType by remember {
        mutableStateOf("")
    }

    var selectedCategoryId by remember {
        mutableStateOf("")
    }

    var expectedDate by remember {
        mutableStateOf("")
    }

    var contactPersonName by remember {
        mutableStateOf("")
    }

    var contactPersonPhone by remember {
        mutableStateOf("")
    }

    var routeExpanded by remember {
        mutableStateOf(false)
    }

    var parcelTypeExpanded by remember {
        mutableStateOf(false)
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    // ============================================================
    // PARCEL TYPES
    // ============================================================

    val parcelTypes = listOf(
        "Documents",
        "Clothes",
        "Food Items",
        "Electronics",
        "Box",
        "Other"
    )

    // ============================================================
    // VIEWMODEL STATE
    // ============================================================

    val routes by parcelViewModel.routes.collectAsState()

    val categories by
    parcelViewModel.weightCategories.collectAsState()

    val fares by
    parcelViewModel.parcelFares.collectAsState()

    val isLoading by
    parcelViewModel.isLoading.collectAsState()

    // ============================================================
    // LOAD DATA
    // ============================================================

    LaunchedEffect(Unit) {
        parcelViewModel.loadRoutes()
        parcelViewModel.loadWeightCategories()
    }

    // ============================================================
    // SELECTED CATEGORY
    // ============================================================

    val selectedCategory =
        categories.firstOrNull {
            it.weightCategoryId == selectedCategoryId
        }

    // ============================================================
    // SELECTED FARE
    // ============================================================

    val selectedFare =
        fares.firstOrNull {
            it.weightCategoryId == selectedCategoryId
        }

    // ============================================================
    // DATE
    // ============================================================

    val context = LocalContext.current

    val dateFormatter = remember {
        SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )
    }

    // ============================================================
    // SCREEN
    // ============================================================

    Scaffold(

        // ========================================================
        // TOP BAR
        // ========================================================

        topBar = {

            Column {

                TopAppBar(

                    title = {

                        Column {

                            Text(
                                text = "Book Your Parcel",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Enter Parcel details",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = onBackClick
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },

                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary,

                            titleContentColor =
                                MaterialTheme.colorScheme.onPrimary,

                            navigationIconContentColor =
                                MaterialTheme.colorScheme.onPrimary
                        ),

                    modifier =
                        Modifier.height(90.dp)
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )
            }
        },

        // ========================================================
        // BOTTOM BUTTON
        // ========================================================

        bottomBar = {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 10.dp,
                            bottom = 60.dp
                        )
                ) {

                    if (errorMessage.isNotBlank()) {

                        Text(
                            text = errorMessage,
                            color =
                                MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier =
                                Modifier.padding(
                                    bottom = 8.dp
                                )
                        )
                    }

                    Button(

                        onClick = {

                            when {

                                selectedRoute == null -> {

                                    errorMessage =
                                        "Please select a route"
                                }

                                selectedDirection.isBlank() -> {

                                    errorMessage =
                                        "Please select route direction"
                                }

                                expectedDate.isBlank() -> {

                                    errorMessage =
                                        "Please select expected date"
                                }

                                parcelType.isBlank() -> {

                                    errorMessage =
                                        "Please select parcel type"
                                }

                                contactPersonName
                                    .trim()
                                    .isBlank() -> {

                                    errorMessage =
                                        "Please enter contact person name"
                                }

                                contactPersonPhone.length != 10 -> {

                                    errorMessage =
                                        "Please enter a valid 10 digit contact phone number"
                                }

                                selectedCategory == null -> {

                                    errorMessage =
                                        "Please select weight category"
                                }

                                selectedFare == null -> {

                                    errorMessage =
                                        "Parcel fare is not available"
                                }

                                else -> {

                                    errorMessage = ""

                                    // Save selection temporarily.
                                    // Contact details are also stored
                                    // for the Review screen.

                                    parcelViewModel.setParcelSelection(

                                        route =
                                            selectedRoute!!,

                                        direction =
                                            selectedDirection,

                                        parcelType =
                                            parcelType.trim(),

                                        categoryId =
                                            selectedCategoryId,

                                        expectedDate =
                                            expectedDate,

                                        fare =
                                            selectedFare!!,

                                        contactPersonName =
                                            contactPersonName.trim(),

                                        contactPersonPhone =
                                            contactPersonPhone.trim()
                                    )

                                    onContinueClick()
                                }
                            }
                        },

                        enabled =
                            !isLoading &&
                                    selectedRoute != null &&
                                    selectedDirection.isNotBlank() &&
                                    expectedDate.isNotBlank() &&
                                    parcelType.isNotBlank() &&
                                    contactPersonName.isNotBlank() &&
                                    contactPersonPhone.length == 10 &&
                                    selectedCategory != null &&
                                    selectedFare != null,

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),

                        shape =
                            RoundedCornerShape(16.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                disabledContainerColor =
                                    Color.LightGray
                            )
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text(
                                text = "Review Parcel",
                                fontSize = 18.sp,
                                fontWeight =
                                    FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        },

        containerColor =
            Color(0xFFF6F8FC)

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 15.dp,
                    bottom = 30.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // ====================================================
            // ROUTE
            // ====================================================

            Text(
                text = "Select Route",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(

                    value = selectedSubRoute,

                    onValueChange = {},

                    readOnly = true,

                    placeholder = {
                        Text("Choose your route")
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint =
                                Color(0xFF2962FF)
                        )
                    },

                    trailingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },

                    shape =
                        RoundedCornerShape(18.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedBorderColor =
                                Color(0xFF2962FF),

                            unfocusedBorderColor =
                                Color(0xFFE5E7EB),

                            focusedContainerColor =
                                Color.White,

                            unfocusedContainerColor =
                                Color.White
                        ),

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            routeExpanded = true
                        }
                )

                DropdownMenu(

                    expanded =
                        routeExpanded,

                    onDismissRequest = {
                        routeExpanded = false
                    },

                    modifier =
                        Modifier.fillMaxWidth()

                ) {

                    if (routes.isEmpty()) {

                        DropdownMenuItem(

                            text = {

                                Text(
                                    text =
                                        "No routes available",
                                    color =
                                        Color.Gray
                                )
                            },

                            onClick = {
                                routeExpanded = false
                            }
                        )

                    } else {

                        routes.forEach { route ->

                            // ====================================
                            // UP ROUTE
                            // ====================================

                            if (route.up.isNotBlank()) {

                                DropdownMenuItem(

                                    text = {

                                        Text(
                                            text = route.up,
                                            fontSize = 14.sp,
                                            fontWeight =
                                                FontWeight.Medium
                                        )
                                    },

                                    onClick = {

                                        selectedRoute = route

                                        selectedDirection =
                                            "UP"

                                        selectedSubRoute =
                                            route.up

                                        selectedCategoryId =
                                            ""

                                        routeExpanded = false

                                        errorMessage = ""

                                        parcelViewModel
                                            .loadParcelFares(
                                                routeId =
                                                    route.routeId,
                                                direction =
                                                    "UP"
                                            )
                                    }
                                )
                            }

                            // ====================================
                            // DOWN ROUTE
                            // ====================================

                            if (route.down.isNotBlank()) {

                                DropdownMenuItem(

                                    text = {

                                        Text(
                                            text = route.down,
                                            fontSize = 14.sp,
                                            fontWeight =
                                                FontWeight.Medium
                                        )
                                    },

                                    onClick = {

                                        selectedRoute = route

                                        selectedDirection =
                                            "DOWN"

                                        selectedSubRoute =
                                            route.down

                                        selectedCategoryId =
                                            ""

                                        routeExpanded = false

                                        errorMessage = ""

                                        parcelViewModel
                                            .loadParcelFares(
                                                routeId =
                                                    route.routeId,
                                                direction =
                                                    "DOWN"
                                            )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // ====================================================
            // EXPECTED DATE
            // ====================================================

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Expected Date",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(

                    value = expectedDate,

                    onValueChange = {},

                    readOnly = true,

                    placeholder = {
                        Text("Select expected date")
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint =
                                Color(0xFF2962FF)
                        )
                    },

                    shape =
                        RoundedCornerShape(18.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedBorderColor =
                                Color(0xFF2962FF),

                            unfocusedBorderColor =
                                Color(0xFFE5E7EB),

                            focusedContainerColor =
                                Color.White,

                            unfocusedContainerColor =
                                Color.White
                        ),

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            showDatePicker = true
                        }
                )
            }

            // ====================================================
            // PARCEL TYPE
            // ====================================================

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Parcel Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(

                    value = parcelType,

                    onValueChange = {},

                    readOnly = true,

                    placeholder = {
                        Text("Select parcel type")
                    },

                    trailingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },

                    shape =
                        RoundedCornerShape(18.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedBorderColor =
                                Color(0xFF2962FF),

                            unfocusedBorderColor =
                                Color(0xFFE5E7EB),

                            focusedContainerColor =
                                Color.White,

                            unfocusedContainerColor =
                                Color.White
                        ),

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            parcelTypeExpanded = true
                        }
                )

                DropdownMenu(

                    expanded =
                        parcelTypeExpanded,

                    onDismissRequest = {
                        parcelTypeExpanded = false
                    },

                    modifier =
                        Modifier.fillMaxWidth()

                ) {

                    parcelTypes.forEach { type ->

                        DropdownMenuItem(

                            text = {
                                Text(type)
                            },

                            onClick = {

                                parcelType = type

                                parcelTypeExpanded =
                                    false

                                errorMessage = ""
                            }
                        )
                    }
                }
            }

            // ====================================================
            // CONTACT PERSON NAME
            // ====================================================

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Contact Person",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedTextField(

                value = contactPersonName,

                onValueChange = {
                    contactPersonName = it
                    errorMessage = ""
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Contact Person Name")
                },

                placeholder = {
                    Text("Enter contact person name")
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text,
                        imeAction =
                            ImeAction.Next
                    ),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        focusedBorderColor =
                            Color(0xFF2962FF),

                        unfocusedBorderColor =
                            Color(0xFFE5E7EB),

                        focusedContainerColor =
                            Color.White,

                        unfocusedContainerColor =
                            Color.White
                    )
            )

            // ====================================================
            // CONTACT PERSON PHONE
            // ====================================================

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(

                value = contactPersonPhone,

                onValueChange = { value ->

                    if (
                        value.length <= 10 &&
                        value.all { it.isDigit() }
                    ) {

                        contactPersonPhone =
                            value

                        errorMessage = ""
                    }
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text("Contact Person Phone")
                },

                placeholder = {
                    Text("Enter 10 digit phone number")
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Phone,
                        imeAction =
                            ImeAction.Done
                    ),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        focusedBorderColor =
                            Color(0xFF2962FF),

                        unfocusedBorderColor =
                            Color(0xFFE5E7EB),

                        focusedContainerColor =
                            Color.White,

                        unfocusedContainerColor =
                            Color.White
                    )
            )

            // ====================================================
            // WEIGHT CATEGORY
            // ====================================================

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Weight Category",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(

                    value =
                        selectedCategory?.categoryName
                            ?: "",

                    onValueChange = {},

                    readOnly = true,

                    placeholder = {
                        Text("Select weight category")
                    },

                    trailingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },

                    shape =
                        RoundedCornerShape(18.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedBorderColor =
                                Color(0xFF2962FF),

                            unfocusedBorderColor =
                                Color(0xFFE5E7EB),

                            focusedContainerColor =
                                Color.White,

                            unfocusedContainerColor =
                                Color.White
                        ),

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {

                            if (selectedRoute != null) {
                                categoryExpanded = true
                            }
                        }
                )

                DropdownMenu(

                    expanded =
                        categoryExpanded,

                    onDismissRequest = {
                        categoryExpanded = false
                    },

                    modifier =
                        Modifier.fillMaxWidth()

                ) {

                    if (fares.isEmpty()) {

                        DropdownMenuItem(

                            text = {

                                Text(
                                    text =
                                        if (
                                            selectedRoute == null
                                        ) {
                                            "Select route first"
                                        } else {
                                            "No fares available"
                                        },

                                    color =
                                        Color.Gray
                                )
                            },

                            onClick = {
                                categoryExpanded =
                                    false
                            }
                        )

                    } else {

                        fares.forEach { fare ->

                            DropdownMenuItem(

                                text = {

                                    Column {

                                        Text(
                                            text =
                                                fare.categoryName,
                                            fontWeight =
                                                FontWeight.SemiBold
                                        )

                                        Text(
                                            text =
                                                "₹${fare.minFare} - ₹${fare.maxFare}",
                                            fontSize =
                                                12.sp,
                                            color =
                                                Color.Gray
                                        )
                                    }
                                },

                                onClick = {

                                    selectedCategoryId =
                                        fare.weightCategoryId

                                    categoryExpanded =
                                        false

                                    errorMessage = ""
                                }
                            )
                        }
                    }
                }
            }

            // ====================================================
            // ESTIMATED FARE
            // ====================================================

            selectedFare?.let { fare ->

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        ),

                    border =
                        BorderStroke(
                            1.dp,
                            Color(0xFFE5E7EB)
                        )
                ) {

                    Row(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Estimated Fare",
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Text(
                            text =
                                "₹${fare.minFare} - ₹${fare.maxFare}",

                            fontWeight =
                                FontWeight.Bold,

                            color =
                                Color(0xFF2962FF)
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )
        }
    }

    // ============================================================
    // DATE PICKER
    // ============================================================

    if (showDatePicker) {

        val calendar =
            Calendar.getInstance()

        val today =
            Calendar.getInstance()

        DatePickerDialog(

            context,

            { _, year, month, day ->

                calendar.set(
                    year,
                    month,
                    day
                )

                expectedDate =
                    dateFormatter.format(
                        calendar.time
                    )

                errorMessage = ""

                showDatePicker = false
            },

            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        ).apply {

            today.set(
                Calendar.HOUR_OF_DAY,
                0
            )

            today.set(
                Calendar.MINUTE,
                0
            )

            today.set(
                Calendar.SECOND,
                0
            )

            today.set(
                Calendar.MILLISECOND,
                0
            )

            datePicker.minDate =
                today.timeInMillis

            datePicker.maxDate =
                today.timeInMillis +
                        (3L * 24L * 60L * 60L * 1000L)

        }.show()
    }
}
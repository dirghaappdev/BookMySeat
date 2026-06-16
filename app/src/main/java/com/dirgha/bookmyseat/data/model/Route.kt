package com.dirgha.bookmyseat.data.model

data class Route(
    val routeId: String,
    val routeName: String,
    val up: String,
    val down: String,
    val active: Boolean
)
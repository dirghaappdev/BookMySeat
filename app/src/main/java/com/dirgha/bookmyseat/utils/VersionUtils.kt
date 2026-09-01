package com.dirgha.bookmyseat.utils

object VersionUtils {

    fun compareVersions(current: String, target: String): Int {

        val currentParts = current.split(".").map { it.toInt() }
        val targetParts = target.split(".").map { it.toInt() }

        val maxLength = maxOf(currentParts.size, targetParts.size)

        for (i in 0 until maxLength) {

            val currentValue = currentParts.getOrElse(i) { 0 }
            val targetValue = targetParts.getOrElse(i) { 0 }

            if (currentValue < targetValue) return -1
            if (currentValue > targetValue) return 1
        }

        return 0
    }
}
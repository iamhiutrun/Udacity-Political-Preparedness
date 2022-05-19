package com.example.android.politicalpreparedness.network.models

import com.example.android.politicalpreparedness.representative.model.Representative
import com.squareup.moshi.Json

data class RepresentativeResponse(
        val offices: List<Office>,
        val officials: List<Official>
) {
    fun mapResponseToRepresentatives(): List<Representative> {
        return officials.mapIndexed { index, official ->
            Representative(
                official,
                offices.first { it.officials.contains(index) }
            )
        }
    }
}
package com.example.test.utils

import com.example.test.data.model.UserPointModel
import com.example.test.presentation.MainActivityViewModel
import org.osmdroid.views.MapView

data class ListenerMarker(private val viewModel: MainActivityViewModel): CustomMarker.OnMarkerClickListener {
    override fun onMarkerClick(marker: CustomMarker, mapView: MapView): Boolean {
        viewModel.clickOnMarker(marker)
        return true
    }
}
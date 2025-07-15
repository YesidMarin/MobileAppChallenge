package com.yesidmarin.mobileappchallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel()  {

    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> get() = _navigationEvent

    fun navigateTo(destination: String) {
        _navigationEvent.value = destination
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

}
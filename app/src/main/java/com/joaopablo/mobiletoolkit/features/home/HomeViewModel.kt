package com.joaopablo.mobiletoolkit.features.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _currentTab = MutableStateFlow("home_tab")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    private val _hasNotification = MutableStateFlow(true) // Initialized to true to show the premium badge dot
    val hasNotification: StateFlow<Boolean> = _hasNotification.asStateFlow()

    fun updateSelectedTab(tabRoute: String) {
        _currentTab.value = tabRoute
    }

    fun toggleNotificationBadge() {
        _currentTab.value = "profile_tab" // Optional interaction
    }

    fun clearNotificationBadge() {
        _hasNotification.value = false
    }

    fun addNotificationBadge() {
        _hasNotification.value = true
    }
}

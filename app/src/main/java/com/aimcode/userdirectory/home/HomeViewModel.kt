package com.aimcode.userdirectory.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimcode.userdirectory.core.data.repository.UserRepository
import com.aimcode.userdirectory.core.model.Resource
import com.aimcode.userdirectory.core.model.response.CityResponse
import com.aimcode.userdirectory.core.model.response.UserResponse
import com.aimcode.userdirectory.core.ui.UiLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel() {

    private var _userState by mutableStateOf<UiLoadState<List<UserResponse>>>(UiLoadState.Idle())
    val userState get() = _userState

    private var _cityState by mutableStateOf<UiLoadState<List<CityResponse>>>(UiLoadState.Idle())
    val cityState get() = _cityState

    var selectedCity by mutableStateOf<CityResponse?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var sortAscending by mutableStateOf(true)
        private set

    private var rawUsers by mutableStateOf<List<UserResponse>>(emptyList())

    val filteredUsers: List<UserResponse>
        get() {
            val searched = if (searchQuery.isBlank()) rawUsers
            else rawUsers.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
            return if (sortAscending) {
                searched.sortedBy { it.name.trim().lowercase() }
            } else {
                searched.sortedByDescending { it.name.trim().lowercase() }
            }
        }

    var isRefreshing by mutableStateOf(false)
        private set

    var isOfflineCache by mutableStateOf(false)
        private set

    init {
        getUsers()
        getCities()
    }

    fun getUsers(cityName: String? = null) {
        viewModelScope.launch {
            _userState = UiLoadState.Loading()
            _userState = when (val result = userRepository.getUsers(cityName)) {
                is Resource.Success -> {
                    rawUsers = result.data.orEmpty()
                    isOfflineCache = result.fromCache
                    UiLoadState.Success(result.data)
                }
                is Resource.Failed -> UiLoadState.Failed(result.code)
            }
        }
    }

    fun getCities() {
        viewModelScope.launch {
            _cityState = UiLoadState.Loading()
            _cityState = when (val result = userRepository.getCities()) {
                is Resource.Success -> {
                    if (result.fromCache) isOfflineCache = true
                    UiLoadState.Success(result.data)
                }
                is Resource.Failed -> UiLoadState.Failed(result.code)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing = true
            val cityName = selectedCity?.name
            val usersJob = launch {
                when (val result = userRepository.getUsers(cityName)) {
                    is Resource.Success -> {
                        rawUsers = result.data.orEmpty()
                        isOfflineCache = result.fromCache
                        _userState = UiLoadState.Success(result.data)
                    }
                    is Resource.Failed -> _userState = UiLoadState.Failed(result.code)
                }
            }
            val citiesJob = launch {
                _cityState = when (val result = userRepository.getCities()) {
                    is Resource.Success -> UiLoadState.Success(result.data)
                    is Resource.Failed -> UiLoadState.Failed(result.code)
                }
            }
            usersJob.join()
            citiesJob.join()
            isRefreshing = false
        }
    }

    fun selectCity(city: CityResponse?) {
        selectedCity = city
        getUsers(city?.name)
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun toggleSort() {
        sortAscending = !sortAscending
    }
}
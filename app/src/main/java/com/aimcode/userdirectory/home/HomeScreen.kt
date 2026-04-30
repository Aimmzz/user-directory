package com.aimcode.userdirectory.home

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.aimcode.userdirectory.core.model.response.CityResponse
import com.aimcode.userdirectory.core.model.response.UserResponse
import com.aimcode.userdirectory.core.ui.UiLoadState
import com.aimcode.userdirectory.core.ui.component.ErrorUi
import com.aimcode.userdirectory.core.ui.component.SkeletonItem
import com.aimcode.userdirectory.home.component.FilterCityBottomSheet
import com.aimcode.userdirectory.home.component.ItemUser
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navBackStackEntry: NavBackStackEntry,
    onNavigateToAddUser: () -> Unit,
) {
    val userAdded by navBackStackEntry.savedStateHandle
        .getStateFlow("user_added", false)
        .collectAsStateWithLifecycle()

    LaunchedEffect(userAdded) {
        if (userAdded) {
            viewModel.refresh()
            navBackStackEntry.savedStateHandle["user_added"] = false
        }
    }

    HomeScreenUi(
        userState = viewModel.userState,
        cityState = viewModel.cityState,
        filteredUsers = viewModel.filteredUsers,
        selectedCity = viewModel.selectedCity,
        searchQuery = viewModel.searchQuery,
        sortAscending = viewModel.sortAscending,
        isRefreshing = viewModel.isRefreshing,
        onRefresh = viewModel::refresh,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onToggleSort = viewModel::toggleSort,
        onSelectCity = viewModel::selectCity,
        onRetry = { viewModel.getUsers(viewModel.selectedCity?.name) },
        onRetryCity = { viewModel.getCities() },
        onNavigateToAddUser = onNavigateToAddUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenUi(
    modifier: Modifier = Modifier,
    userState: UiLoadState<List<UserResponse>>,
    cityState: UiLoadState<List<CityResponse>>,
    filteredUsers: List<UserResponse>,
    selectedCity: CityResponse?,
    searchQuery: String,
    sortAscending: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleSort: () -> Unit,
    onSelectCity: (CityResponse?) -> Unit,
    onRetry: () -> Unit,
    onRetryCity: () -> Unit,
    onNavigateToAddUser: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        FilterCityBottomSheet(
            sheetState = sheetState,
            cityState = cityState,
            selectedCity = selectedCity,
            onSelectCity = { city ->
                onSelectCity(city)
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    showBottomSheet = false
                }
            },
            onDismiss = { showBottomSheet = false },
            onRetry = onRetryCity,
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("User Directory") },
                    actions = {
                        IconButton(onClick = onToggleSort) {
                            Icon(
                                imageVector = if (sortAscending)
                                    Icons.Rounded.SortByAlpha
                                else
                                    Icons.Rounded.SortByAlpha,
                                contentDescription = "Urutkan",
                                tint = if (sortAscending)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }

                        BadgedBox(
                            badge = { if (selectedCity != null) Badge() }
                        ) {
                            IconButton(onClick = { showBottomSheet = true }) {
                                Icon(
                                    imageVector = Icons.Rounded.FilterList,
                                    contentDescription = "Filter"
                                )
                            }
                        }

                        IconButton(onClick = onNavigateToAddUser) {
                            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Tambah")
                        }
                    }
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Cari nama user...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Hapus"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(innerPadding)
        ) {
            when (userState) {
                is UiLoadState.Idle,
                is UiLoadState.Loading -> SkeletonUi()

                is UiLoadState.Failed -> ErrorUi(onRetry = onRetry)

                is UiLoadState.Success -> SuccessUi(
                    modifier = modifier,
                    users = filteredUsers,
                    sortAscending = sortAscending
                )
            }
        }
    }
}

@Composable
private fun SkeletonUi(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(8) {
            SkeletonItem()
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
private fun SuccessUi(
    modifier: Modifier = Modifier,
    users: List<UserResponse>,
    sortAscending: Boolean,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${users.size} user",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (sortAscending)
                        Icons.Rounded.ArrowUpward
                    else
                        Icons.Rounded.ArrowDownward,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (sortAscending) "A - Z" else "Z - A",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (users.isEmpty()) {
            item {
                Text(
                    text = "Tidak ada user ditemukan.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        items(users, key = { it.id }) { user ->
            ItemUser(user = user)
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp
            )
        }
    }
}
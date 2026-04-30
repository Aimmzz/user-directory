package com.aimcode.userdirectory.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aimcode.userdirectory.core.model.response.CityResponse
import com.aimcode.userdirectory.core.ui.UiLoadState
import com.aimcode.userdirectory.core.ui.component.ErrorUi
import com.aimcode.userdirectory.core.ui.component.SkeletonItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCityBottomSheet(
    sheetState: SheetState,
    cityState: UiLoadState<List<CityResponse>>,
    selectedCity: CityResponse?,
    onSelectCity: (CityResponse?) -> Unit,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Kota",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (selectedCity != null) {
                    TextButton(onClick = { onSelectCity(null) }) {
                        Text("Reset")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            when (cityState) {
                is UiLoadState.Idle,
                is UiLoadState.Loading -> {
                    repeat(5) {
                        CitySkeletonItem()
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp
                        )
                    }
                }

                is UiLoadState.Failed -> {
                    ErrorUi(onRetry = onRetry)
                }

                is UiLoadState.Success -> {
                    val cities = cityState.data.orEmpty()
                    LazyColumn {
                        items(cities, key = { it.id }) { city ->
                            CityItem(
                                city = city,
                                isSelected = selectedCity?.id == city.id,
                                onClick = { onSelectCity(city) }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CityItem(
    city: CityResponse,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = city.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CitySkeletonItem() {
    SkeletonItem()
}
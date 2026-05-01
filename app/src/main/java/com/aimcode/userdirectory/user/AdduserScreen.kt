package com.aimcode.userdirectory.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aimcode.userdirectory.core.model.response.UserResponse
import com.aimcode.userdirectory.core.ui.UiLoadState
import com.aimcode.userdirectory.core.ui.component.LoadingBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    onNavigateBack: () -> Unit,
    onUserAdded: () -> Unit,
    viewModel: AddUserViewModel = hiltViewModel()
) {
    val addUserState = viewModel.addUserState
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(addUserState) {
        when (addUserState) {
            is UiLoadState.Loading -> {
                coroutineScope.launch { sheetState.show() }
            }
            is UiLoadState.Success -> {
                coroutineScope.launch {
                    sheetState.hide()
                    onUserAdded()
                }
            }
            is UiLoadState.Failed -> {
                coroutineScope.launch { sheetState.hide() }
            }
            else -> {
                coroutineScope.launch { sheetState.hide() }
            }
        }
    }

    AddUserScreenUi(
        name = viewModel.name,
        address = viewModel.address,
        email = viewModel.email,
        phoneNumber = viewModel.phoneNumber,
        city = viewModel.city,
        gender = viewModel.gender,
        isFormValid = viewModel.isFormValid,
        addUserState = addUserState,
        onNameChange = viewModel::onNameChange,
        onAddressChange = viewModel::onAddressChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onCityChange = viewModel::onCityChange,
        onGenderChange = viewModel::onGenderChange,
        onSubmit = viewModel::addUser,
        onResetState = viewModel::resetState,
        onNavigateBack = onNavigateBack
    )

    LoadingBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUserScreenUi(
    modifier: Modifier = Modifier,
    name: String,
    address: String,
    email: String,
    phoneNumber: String,
    city: String,
    gender: Int,
    isFormValid: Boolean,
    addUserState: UiLoadState<UserResponse>,
    onNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onGenderChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    onResetState: () -> Unit,
    onNavigateBack: () -> Unit,
) {

    if (addUserState is UiLoadState.Failed) {
        AlertDialog(
            onDismissRequest = onResetState,
            title = { Text("Gagal Menambahkan User") },
            text = { Text("Terjadi kesalahan saat menyimpan data. Silakan coba lagi.") },
            confirmButton = {
                TextButton(onClick = onResetState) {
                    Text("Tutup")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah User") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nama") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text("Nomor Telepon") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            OutlinedTextField(
                value = address,
                onValueChange = onAddressChange,
                label = { Text("Alamat") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = city,
                onValueChange = onCityChange,
                label = { Text("Kota") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Jenis Kelamin",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = gender == 1,
                        onClick = { onGenderChange(1) },
                        label = { Text("Pria") }
                    )
                    FilterChip(
                        selected = gender == 0,
                        onClick = { onGenderChange(0) },
                        label = { Text("Wanita") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid && addUserState !is UiLoadState.Loading
            ) {
                Text("Simpan")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
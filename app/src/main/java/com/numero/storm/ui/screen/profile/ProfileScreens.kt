package com.numero.storm.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numero.storm.R
import com.numero.storm.data.model.Profile
import com.numero.storm.ui.components.LoadingIndicator
import com.numero.storm.ui.components.NumeroTopBar
import com.numero.storm.ui.viewmodel.CreateProfileViewModel
import com.numero.storm.ui.viewmodel.EditProfileViewModel
import com.numero.storm.ui.viewmodel.ProfileListViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToAnalysis: (Long) -> Unit,
    viewModel: ProfileListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var profileToDelete by remember { mutableStateOf<Profile?>(null) }

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.profiles),
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_profile))
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        } else if (uiState.profiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.no_profiles_yet),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.create_first_profile),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.profiles, key = { it.id }) { profile ->
                    ProfileListItem(
                        profile = profile,
                        onClick = { onNavigateToAnalysis(profile.id) },
                        onEdit = { onNavigateToEdit(profile.id) },
                        onSetPrimary = { viewModel.setAsPrimary(profile.id) },
                        onDelete = { profileToDelete = profile }
                    )
                }
            }
        }
    }

    profileToDelete?.let { profile ->
        AlertDialog(
            onDismissRequest = { profileToDelete = null },
            title = { Text(stringResource(R.string.delete_profile)) },
            text = { Text(stringResource(R.string.delete_profile_confirm, profile.displayName)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProfile(profile)
                        profileToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { profileToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun ProfileListItem(
    profile: Profile,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onSetPrimary: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (profile.isPrimary) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (profile.isPrimary) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondaryContainer
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.firstName.first().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (profile.isPrimary) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (profile.isPrimary) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.primary_profile),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = profile.birthDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!profile.isPrimary) {
                IconButton(onClick = onSetPrimary) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.set_as_primary),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    onNavigateBack: () -> Unit,
    onProfileCreated: (Long) -> Unit,
    viewModel: CreateProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved, uiState.savedProfileId) {
        if (uiState.isSaved && uiState.savedProfileId != null) {
            onProfileCreated(uiState.savedProfileId!!)
        }
    }

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.create_profile),
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        ProfileForm(
            modifier = Modifier.padding(padding),
            firstName = uiState.firstName,
            middleName = uiState.middleName,
            lastName = uiState.lastName,
            birthDate = uiState.birthDate,
            isPrimary = uiState.isPrimary,
            firstNameError = uiState.firstNameError,
            lastNameError = uiState.lastNameError,
            birthDateError = uiState.birthDateError,
            isLoading = uiState.isLoading,
            onFirstNameChange = viewModel::updateFirstName,
            onMiddleNameChange = viewModel::updateMiddleName,
            onLastNameChange = viewModel::updateLastName,
            onBirthDateChange = viewModel::updateBirthDate,
            onIsPrimaryChange = viewModel::updateIsPrimary,
            onSave = viewModel::saveProfile,
            saveButtonText = stringResource(R.string.create_profile)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileId: Long,
    onNavigateBack: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(profileId) {
        viewModel.loadProfile(profileId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            NumeroTopBar(
                title = stringResource(R.string.edit_profile),
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading && uiState.firstName.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        } else {
            ProfileForm(
                modifier = Modifier.padding(padding),
                firstName = uiState.firstName,
                middleName = uiState.middleName,
                lastName = uiState.lastName,
                birthDate = uiState.birthDate,
                isPrimary = uiState.isPrimary,
                firstNameError = uiState.firstNameError,
                lastNameError = uiState.lastNameError,
                birthDateError = uiState.birthDateError,
                isLoading = uiState.isLoading,
                onFirstNameChange = viewModel::updateFirstName,
                onMiddleNameChange = viewModel::updateMiddleName,
                onLastNameChange = viewModel::updateLastName,
                onBirthDateChange = viewModel::updateBirthDate,
                onIsPrimaryChange = viewModel::updateIsPrimary,
                onSave = viewModel::saveProfile,
                saveButtonText = stringResource(R.string.save_changes)
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_profile)) },
            text = { Text(stringResource(R.string.delete_profile_confirm, uiState.firstName)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProfile()
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileForm(
    modifier: Modifier = Modifier,
    firstName: String,
    middleName: String,
    lastName: String,
    birthDate: LocalDate?,
    isPrimary: Boolean,
    firstNameError: String?,
    lastNameError: String?,
    birthDateError: String?,
    isLoading: Boolean,
    onFirstNameChange: (String) -> Unit,
    onMiddleNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onBirthDateChange: (LocalDate?) -> Unit,
    onIsPrimaryChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    saveButtonText: String
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = birthDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = { Text(stringResource(R.string.first_name)) },
            modifier = Modifier.fillMaxWidth(),
            isError = firstNameError != null,
            supportingText = firstNameError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = middleName,
            onValueChange = onMiddleNameChange,
            label = { Text(stringResource(R.string.middle_name_optional)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text(stringResource(R.string.last_name)) },
            modifier = Modifier.fillMaxWidth(),
            isError = lastNameError != null,
            supportingText = lastNameError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = birthDate?.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) ?: "",
            onValueChange = { },
            label = { Text(stringResource(R.string.birth_date)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            enabled = false,
            isError = birthDateError != null,
            supportingText = birthDateError?.let { { Text(it) } },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = stringResource(R.string.select_date)
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onIsPrimaryChange(!isPrimary) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isPrimary,
                onCheckedChange = onIsPrimaryChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.set_as_primary_profile),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                LoadingIndicator()
            } else {
                Text(saveButtonText)
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onBirthDateChange(date)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

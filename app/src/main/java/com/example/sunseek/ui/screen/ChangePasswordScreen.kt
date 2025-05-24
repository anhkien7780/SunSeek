@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.sunseek.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunseek.R
import com.example.sunseek.model.LoadingUIState
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.ForgetPasswordViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object ChangePassword

@Composable
fun ChangePasswordScreen(
    forgetPasswordViewModel: ForgetPasswordViewModel,
    onBack: () -> Unit,
    onChangePasswordSuccess: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back_button
                            )
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.create_new_password),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        val loadingUIState by forgetPasswordViewModel.loadingUIState.collectAsState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val scope = rememberCoroutineScope()
            var newPassword by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }
            val context = LocalContext.current
            Spacer(Modifier.padding(5.dp))

            // New Password Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = { Text(text = stringResource(R.string.enter_new_password)) },
                visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    val icon =
                        if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off
                    val description =
                        if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                            R.string.show_password
                        )
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(icon), contentDescription = description)
                    }
                },
                singleLine = true
            )
            Spacer(Modifier.padding(5.dp))

            // Confirm New Password Field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text(text = stringResource(R.string.confirm_new_password)) },
                visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    val icon =
                        if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off
                    val description =
                        if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                            R.string.show_password
                        )
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(icon), contentDescription = description)
                    }
                },
                isError = if (confirmPassword.isNotEmpty()) newPassword != confirmPassword else false,
                singleLine = true
            )
            Spacer(Modifier.padding(5.dp))

            // Confirm Button
            Button(
                onClick = {
                    scope.launch {
                        forgetPasswordViewModel.changePasswordRequest(context, newPassword)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = confirmPassword == newPassword
            ) {
                Text(
                    text = stringResource(R.string.change_password),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

        }
        when (loadingUIState) {
            LoadingUIState.Loading -> FullScreenLoading(stringResource(R.string.change_password))
            LoadingUIState.Idle -> {}
            LoadingUIState.Success -> {}
            LoadingUIState.Failed -> {}
        }
        LaunchedEffect(loadingUIState) {
            if (loadingUIState == LoadingUIState.Success) {
                onChangePasswordSuccess()
                forgetPasswordViewModel.setIdle()
            }
        }
    }
}

@Preview
@Composable
fun ChangePasswordScreenPreview() {
    SunSeekTheme {
        ChangePasswordScreen(
            onBack = {},
            forgetPasswordViewModel = viewModel()
        ) { }
    }
}
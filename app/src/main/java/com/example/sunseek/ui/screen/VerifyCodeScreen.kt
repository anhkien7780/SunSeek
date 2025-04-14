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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.R
import com.example.sunseek.model.VerifyCodeRequest
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.AccountViewModel
import com.example.sunseek.viewmodel.ForgetPasswordViewModel
import com.example.sunseek.viewmodel.LoadingUIState
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object VerifyCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeScreen(
    forgetPasswordViewModel: ForgetPasswordViewModel,
    accountViewModel: AccountViewModel,
    onBack: () -> Unit,
    onVerifySuccess: () -> Unit
) {
    val email = accountViewModel.email.value
    val loadingUIState by forgetPasswordViewModel.loadingUIState.collectAsState()
    val scope = rememberCoroutineScope()
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current
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
                        text = stringResource(R.string.verify_code),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = code,
                onValueChange = { code = it },
                placeholder = { Text(text = "Nhập mã xác thực") },
                singleLine = true
            )
            Spacer(Modifier.padding(5.dp))
            Button(
                onClick = {
                    scope.launch {
                        forgetPasswordViewModel.verifyCode(context, VerifyCodeRequest(email, code))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
        when (loadingUIState) {
            LoadingUIState.Failed -> {}
            LoadingUIState.Idle -> {}
            LoadingUIState.Loading -> FullScreenLoading(stringResource(R.string.verifying), innerPadding)
            LoadingUIState.Success -> {}
        }
        LaunchedEffect(loadingUIState) {
            if (loadingUIState == LoadingUIState.Success) {
                onVerifySuccess()
                forgetPasswordViewModel.setIdle()
            }
        }
    }
}

@Preview
@Composable
fun VerifyCodeScreenPreview() {
    SunSeekTheme {
        VerifyCodeScreen(
            onBack = {}, onVerifySuccess = {},
            forgetPasswordViewModel = ForgetPasswordViewModel(),
            accountViewModel = AccountViewModel(),
        )
    }
}
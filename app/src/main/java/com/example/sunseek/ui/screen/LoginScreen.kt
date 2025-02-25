@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.sunseek.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunseek.OutlinedTextFieldFloatedLabel
import com.example.sunseek.R
import com.example.sunseek.model.User
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.AccountViewModel
import com.example.sunseek.viewmodel.LoginUIState
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
object Login

enum class Screen {
    Login, Register, ForgetPassword
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    accountViewModel: AccountViewModel,
    onLoginSuccess: () -> Unit,
    context: Context
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var correctPassword by remember { mutableStateOf("") }
    var isVisibility by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    val centerScreen = getScreenCenter()
    val circleX = remember { Animatable(centerScreen) }
    var screen by remember { mutableStateOf(Screen.Login) }
    val coroutineScope = rememberCoroutineScope()
    val right = 300f
    var isLoading by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            screen = Screen.Login
                            coroutineScope.launch {
                                circleX.animateTo(
                                    targetValue = centerScreen,
                                    animationSpec = tween(durationMillis = 1000)
                                )
                            }
                        }
                    ) {
                        if (screen != Screen.Login) Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_the_login_side)
                        )
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    radius = size.width * 1f,
                    center = Offset(circleX.value, size.height / 2),
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(242, 154, 119, 100),
                            Color(247, 222, 212, 100)
                        ),
                        start = Offset(0f, 0f), end = Offset(size.width, size.height)
                    )
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.sunseek),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 116.dp)
                )
                OutlinedTextFieldFloatedLabel(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) {
                                isEmailValid = if (email.isNotEmpty())
                                    validateEmail(email)
                                else
                                    true
                            } else if (focusState.isFocused) {
                                isEmailValid = true
                            }
                        },
                    value = email,
                    onValueChange = { email = it },
                    labelText = stringResource(R.string.account),
                    placeholder = { Text(stringResource(R.string.enter_account)) },
                    prefix = {
                        Icon(
                            Icons.Rounded.AccountCircle,
                            contentDescription = stringResource(R.string.account_circle)
                        )
                    },
                    isError = !isEmailValid
                )
                AnimatedVisibility(
                    screen == Screen.Login || screen == Screen.Register,
                    enter = fadeIn() + expandVertically(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    OutlinedTextFieldFloatedLabel(
                        value = password,
                        onValueChange = { password = it },
                        labelText = stringResource(R.string.password),
                        placeholder = { Text(stringResource(R.string.enter_password)) },
                        prefix = {
                            Icon(
                                Icons.Rounded.Lock,
                                contentDescription = stringResource(R.string.account_circle)
                            )
                        },
                        suffix = {
                            IconButton(
                                onClick = {
                                    isVisibility = !isVisibility
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    painter = if (!isVisibility) painterResource(R.drawable.visibility_off)
                                    else painterResource(R.drawable.visibility),
                                    contentDescription = "Password prefix icon",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (!isVisibility) PasswordVisualTransformation()
                        else VisualTransformation.None
                    )
                }
                AnimatedVisibility(
                    screen == Screen.Register,
                    enter = fadeIn() + expandVertically(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    OutlinedTextFieldFloatedLabel(
                        modifier = Modifier.padding(top = 16.dp),
                        value = correctPassword,
                        onValueChange = { correctPassword = it },
                        labelText = stringResource(R.string.password),
                        placeholder = { Text(stringResource(R.string.enter_password_again)) },
                        prefix = {
                            Icon(
                                Icons.Rounded.Lock,
                                contentDescription = stringResource(R.string.prefix_password_icon)
                            )
                        },
                        suffix = {
                            IconButton(
                                onClick = {
                                    isVisibility = !isVisibility
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    painter = if (!isVisibility) painterResource(R.drawable.visibility_off)
                                    else painterResource(R.drawable.visibility),
                                    contentDescription = stringResource(R.string.account_circle),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (!isVisibility) PasswordVisualTransformation()
                        else VisualTransformation.None,
                        isError = correctPassword != password && correctPassword.isNotEmpty()
                    )
                }

                AnimatedVisibility(
                    screen == Screen.Login,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 34.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = {
                                screen = Screen.Register
                                coroutineScope.launch {
                                    circleX.animateTo(
                                        targetValue = circleX.value + right,
                                        animationSpec = tween(durationMillis = 1000)
                                    )
                                }
                            }
                        ) {
                            Text(
                                stringResource(R.string.sign_up),
                                style = MaterialTheme.typography.bodyLarge,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        TextButton(
                            onClick = {
                                screen = Screen.ForgetPassword
                                coroutineScope.launch {
                                    circleX.animateTo(
                                        targetValue = circleX.value - right,
                                        animationSpec = tween(durationMillis = 1000)
                                    )
                                }
                            }
                        ) {
                            Text(
                                stringResource(R.string.forget_password),
                                style = MaterialTheme.typography.bodyLarge,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                // Login Button
                Button(
                    modifier = Modifier.padding(top = 34.dp),
                    onClick = {
                        when (accountViewModel.loginUIState) {
                            LoginUIState.Failed -> {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.login_falied_vn),
                                    Toast.LENGTH_SHORT
                                ).show()
                                isLoading = false
                                accountViewModel.updateLoginUIState(LoginUIState.Idle)
                            }

                            LoginUIState.Loading -> {
                                isLoading = true
                            }

                            LoginUIState.Idle -> {}
                        }
                        when (screen) {
                            Screen.Login -> {
                                coroutineScope.launch {
                                    try {
                                        if (accountViewModel.login(
                                                context = context,
                                                user = User(email, password)
                                            )
                                        ) {
                                            accountViewModel.updateUsername(username = email)
                                            onLoginSuccess()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.login_falied_vn),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                        println(context.getString(R.string.login_failed))
                                    }
                                }
                            }

                            Screen.Register -> {
                                coroutineScope.launch {
                                    try {
                                        accountViewModel.updateLoginUIState(LoginUIState.Loading)
                                        val isLoginSuccess =
                                            accountViewModel.register(User(email, password))
                                        if (isLoginSuccess) {
                                            accountViewModel.updateLoginUIState(
                                                LoginUIState.Idle
                                            )
                                            screen = Screen.Login
                                            coroutineScope.launch {
                                                circleX.animateTo(
                                                    targetValue = centerScreen,
                                                    animationSpec = tween(durationMillis = 1000)
                                                )
                                            }
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.register_success),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else accountViewModel.updateLoginUIState(LoginUIState.Failed)
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                        println(context.getString(R.string.register_failed))
                                    }

                                }
                            }

                            Screen.ForgetPassword -> TODO()
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    when (screen) {
                        Screen.Login -> Text(
                            stringResource(R.string.sign_in),
                            style = MaterialTheme.typography.labelLarge
                        )

                        Screen.Register -> Text(
                            stringResource(R.string.sign_up),
                            style = MaterialTheme.typography.labelLarge
                        )

                        Screen.ForgetPassword -> Text(
                            stringResource(R.string.send_new_password),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

            }
            LoadingOverlay(isLoading)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreView() {
    SunSeekTheme {
        LoginScreen(
            context = LocalContext.current,
            onLoginSuccess = {}, accountViewModel = AccountViewModel(),
        )
    }
}

@Composable
fun getScreenCenter(): Float {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidthDp.toPx() }
    return screenWidthPx / 2
}

fun validateEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return email.matches(emailRegex)
}

@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .blur(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
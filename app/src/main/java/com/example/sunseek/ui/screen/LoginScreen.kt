package com.example.sunseek.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunseek.R
import com.example.sunseek.model.EmailRequest
import com.example.sunseek.model.User
import com.example.sunseek.ui.theme.SunSeekTheme
import com.example.sunseek.viewmodel.AccountViewModel
import com.example.sunseek.viewmodel.LoadingUIState
import com.example.sunseek.viewmodel.LocationViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
object Login

enum class Screen {
    Login, Register, ForgetPassword
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    accountViewModel: AccountViewModel,
    locationViewModel: LocationViewModel,
    onLoginSuccess: () -> Unit,
    onForgetPasswordRequestSuccess: () -> Unit
) {
    val context: Context = LocalContext.current
    val circleRadius = 1150f
    val right = 500f
    val infiniteTransition = rememberInfiniteTransition()
    val colorTop by infiniteTransition.animateColor(
        initialValue = Color(242, 154, 119),
        targetValue = Color(247, 222, 212),
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorAnimation"
    )
    val colorBottom by infiniteTransition.animateColor(
        initialValue = Color(247, 222, 212),
        targetValue = Color(242, 154, 119),
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorAnimation"
    )
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var correctPassword by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    val centerScreen = getScreenCenter()
    val centerPoint = remember { Animatable(centerScreen, Offset.VectorConverter) }
    var screen by remember { mutableStateOf(Screen.Login) }
    val coroutineScope = rememberCoroutineScope()
    val accountLoadingUIState by accountViewModel.loadingUIState.collectAsState()
    val locationLoadingUIState by locationViewModel.loadingUIState.collectAsState()
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
                                centerPoint.animateTo(
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

        // Body
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        radius = circleRadius,
                        center = centerPoint.value,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorTop,
                                colorBottom
                            ),
                            start = Offset(0f, 0f), end = Offset(size.width, size.height)
                        )
                    )
                }
                .padding(horizontal = 20.dp)
        ) {
            // Logo
            Text(
                text = stringResource(R.string.sunseek),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 116.dp)
            )
            // Login Input
            LoginInput(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.padding(bottom = 5.dp),
                onEmailValidChange = { isEmailValid = it }
            )
            // Password Input
            AnimatedVisibility(
                screen == Screen.Login || screen == Screen.Register,
                enter = fadeIn() + expandVertically(),
                exit = shrinkVertically() + fadeOut()
            ) {
                PasswordInput(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }
            // Correct Password Input
            AnimatedVisibility(
                screen == Screen.Register,
                enter = fadeIn() + expandVertically(),
                exit = shrinkVertically() + fadeOut()
            ) {
                CorrectPasswordInput(
                    value = correctPassword,
                    onValueChange = { correctPassword = it },
                    password = password,
                    modifier = Modifier.padding(bottom = 5.dp)
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
                    // Register Button
                    TextButton(
                        onClick = {
                            screen = Screen.Register
                            coroutineScope.launch {
                                centerPoint.animateTo(
                                    targetValue = centerPoint.value.copy(x = centerPoint.value.x + right),
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
                    // Forget Password Button
                    TextButton(
                        onClick = {
                            screen = Screen.ForgetPassword
                            coroutineScope.launch {
                                centerPoint.animateTo(
                                    targetValue = centerPoint.value.copy(centerPoint.value.x - right),
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
                    when (screen) {
                        Screen.Login -> {
                            coroutineScope.launch {
                                if (accountViewModel.login(
                                        User(email, password),
                                        context
                                    )
                                ) {
                                    accountViewModel.updateUsername(username = email)
                                    locationViewModel.getListLocation(context)
                                    onLoginSuccess()
                                }
                            }
                        }

                        Screen.Register -> {
                            coroutineScope.launch {
                                if (accountViewModel.register(
                                        context,
                                        User(email, password)
                                    )
                                ) {
                                    screen = Screen.Login
                                    coroutineScope.launch {
                                        centerPoint.animateTo(
                                            targetValue = centerScreen,
                                            animationSpec = tween(durationMillis = 1000)
                                        )
                                    }
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.register_success),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        Screen.ForgetPassword -> {
                            coroutineScope.launch {
                                accountViewModel.forgetPasswordRequest(
                                    context,
                                    EmailRequest(email)
                                ) {
                                    onForgetPasswordRequestSuccess()
                                }
                            }
                        }
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = isEmailValid && validatePassword(password, correctPassword, screen)
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
                        stringResource(R.string.send_verify_code),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

        }

    }

    when {
        accountLoadingUIState == LoadingUIState.Loading -> FullScreenLoading("Xác thực tài khoản")
        locationLoadingUIState == LoadingUIState.Loading -> FullScreenLoading("Tải danh sách địa chỉ")
    }
}

fun validatePassword(password: String, correctPassword: String, screen: Screen): Boolean {
    if (screen == Screen.Register) {
        return password == correctPassword
    } else {
        return true
    }
}

@Composable
fun LoginInput(
    value: String,
    onValueChange: (String) -> Unit,
    onEmailValidChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf(value) }
    var isEmailValid by remember { mutableStateOf(true) }
    OutlinedTextFieldFloatedLabel(
        modifier = modifier
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    isEmailValid = if (email.isNotEmpty())
                        validateEmail(email)
                    else
                        true
                }
                onEmailValidChange(isEmailValid)
            },
        value = email,
        onValueChange = {
            email = it
            onValueChange(email)
        },
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
}

@Composable
fun PasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf(value) }
    var isVisibility by remember { mutableStateOf(false) }
    OutlinedTextFieldFloatedLabel(
        modifier = modifier,
        value = password,
        onValueChange = {
            password = it
            onValueChange(password)
        },
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

@Composable
fun CorrectPasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    password: String,
    modifier: Modifier = Modifier
) {
    var correctPassword by remember { mutableStateOf(value) }
    var isVisibility by remember { mutableStateOf(false) }
    OutlinedTextFieldFloatedLabel(
        modifier = modifier,
        value = correctPassword,
        onValueChange = {
            correctPassword = it
            onValueChange(correctPassword)
        },
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


@Preview(showBackground = true)
@Composable
fun LoginScreenPreView() {
    SunSeekTheme {
        LoginScreen(
            onLoginSuccess = {}, accountViewModel = AccountViewModel(),
            locationViewModel = LocationViewModel(), onForgetPasswordRequestSuccess = {},
        )
    }
}

@Composable
fun getScreenCenter(): Offset {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidthDp.toPx() }
    val screenHeightPx = with(density) { screenHeightDp.toPx() }
    return Offset(screenWidthPx / 2, screenHeightPx / 2)
}

fun validateEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return email.matches(emailRegex)
}

@Composable
fun OutlinedTextFieldFloatedLabel(
    modifier: Modifier = Modifier,
    labelText: String,
    placeholder: @Composable (() -> Unit)? = null,
    value: String,
    onValueChange: ((String) -> Unit),
    prefix: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    Box {
        Text(
            labelText,
            modifier = Modifier
                .padding(start = 10.dp)
        )
        OutlinedTextField(
            modifier = modifier
                .padding(top = 25.dp)
                .width(343.dp),
            value = value,
            onValueChange = onValueChange,
            maxLines = 1,
            prefix = prefix,
            placeholder = placeholder,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            minLines = minLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextFieldFloatedLabelPreview() {
    SunSeekTheme {
        OutlinedTextFieldFloatedLabel(
            labelText = "Label",
            value = "Value",
            onValueChange = {},
        )
    }
}


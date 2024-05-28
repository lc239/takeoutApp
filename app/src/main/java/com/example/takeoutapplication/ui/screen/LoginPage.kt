package com.example.takeoutapplication.ui.screen

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.takeoutapplication.R
import com.example.takeoutapplication.model.request.LoginRequestBody
import com.example.takeoutapplication.model.request.RegisterUserBody
import com.example.takeoutapplication.ui.components.common.TopEndAndBottomStartMask
import com.example.takeoutapplication.utils.PatternUtils
import com.example.takeoutapplication.viewmodel.LoginUiState
import com.example.takeoutapplication.viewmodel.LoginViewModel
import com.example.takeoutapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(userViewModel: UserViewModel, loginViewModel: LoginViewModel = viewModel()){
    val registerRoleRadios: List<Pair<Int, String>> = listOf(Pair(0, "用户"), Pair(1, "商家"), Pair(2, "骑手"))
    val textFieldColor = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent,
        focusedIndicatorColor = Color.LightGray,
        unfocusedIndicatorColor = Color.LightGray,
        focusedLabelColor = Color.LightGray,
        unfocusedLabelColor = Color.LightGray,
        cursorColor = Color.White
    )
    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        TopEndAndBottomStartMask(
            topEndOffset = Offset(constraints.maxWidth.toFloat(), 0f),
            bottomStartOffset = Offset(0f, constraints.maxHeight.toFloat())
        )
        Text(
            text = "登录", modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp),
            color = Color.White, fontSize = 20.sp
        )
        Column(modifier = Modifier.align(Alignment.Center)) {
            if(loginViewModel.register){
                TextField(
                    value = loginViewModel.username,
                    onValueChange = {
                        if(it.length <= 10){
                            loginViewModel.username = it
                        }
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White)
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.username),
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    },
                    colors = textFieldColor
                )
            }
            TextField(
                value = loginViewModel.phone,
                onValueChange = { loginViewModel.phone = it },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = null, tint = Color.White)
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.phone),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                },
                colors = textFieldColor
            )
            TextField(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.password = it },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.White)
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.password),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                },
                colors = textFieldColor,
                visualTransformation = if(!loginViewModel.showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = { loginViewModel.showPassword = !loginViewModel.showPassword }) {
                        Icon(
                            imageVector = if(loginViewModel.showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "密码显示",
                            tint = Color.White
                        )
                    }
                }
            )
            if(loginViewModel.register){
                Row {
                    registerRoleRadios.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = loginViewModel.role == it.first, onClick = {
                                loginViewModel.role = it.first
                            })
                            Text(text = it.second)
                        }
                    }
                }
            }
            TextButton(
                enabled = userViewModel.loginUiState !is LoginUiState.Loading, //加载时禁用
                onClick = {
                    if(PatternUtils.checkPhone(loginViewModel.phone) && loginViewModel.password.isNotEmpty()){
                        if(loginViewModel.register){
                            if(loginViewModel.username.isNotEmpty()){
                                userViewModel.register(RegisterUserBody(loginViewModel.username, loginViewModel.phone, loginViewModel.password, loginViewModel.role == 1, loginViewModel.role == 2))
                            }
                        }else{
                            userViewModel.login(LoginRequestBody(loginViewModel.phone, loginViewModel.password))
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 6.dp)
            ) {
                Text(
                    text = if(loginViewModel.register) stringResource(id = R.string.register_and_login)
                        else stringResource(id = R.string.login),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
        TextButton(
            onClick = { loginViewModel.register = !loginViewModel.register },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = if(loginViewModel.register) "回到登录" else "还没有账号？点我注册！",
                fontSize = 14.sp, color = Color.White
            )
        }
    }
}
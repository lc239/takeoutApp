package com.example.takeoutapplication.ui.screen.mine

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.takeoutapplication.R
import com.example.takeoutapplication.model.Screen
import com.example.takeoutapplication.utils.AliUtil
import com.example.takeoutapplication.utils.uriToFile
import com.example.takeoutapplication.viewmodel.UserInfoUiState
import com.example.takeoutapplication.viewmodel.UserViewModel

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinePage(userViewModel: UserViewModel, modifier: Modifier, navController: NavController){
    val context = LocalContext.current
    val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    val focusManager = LocalFocusManager.current

    //权限申请
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult ={
            if(!it) userViewModel.showChangeUserAvatarDialog = false
        }
    )
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                val file = uriToFile(context, it)
                if(file != null) userViewModel.updateAvatar(file)
                else userViewModel.showMsg("没有选择图片")
            }
        })
    if(userViewModel.showChangeUserAvatarDialog){
        if(context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED){
            println(1)
        }else{
            launcher.launch(permission)
            println(2)
        }

        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            title = {
                Text(
                    fontSize = 18.sp,
                    text = "确定更改头像吗？"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    selectImageLauncher.launch("image/*") //选取图片
                    userViewModel.showChangeUserAvatarDialog = false
                }) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    userViewModel.showChangeUserAvatarDialog = false
                }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "个人中心", fontSize = 18.sp, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor =Color.Transparent
                ),
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            listOf(
                                colorResource(id = R.color.blue_200),
                                colorResource(id = R.color.blue_500)
                            )
                        )
                    )
                    .height(46.dp)
            )
        },
        snackbarHost = {
            SnackbarHost(userViewModel.snackbarHostState)
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(14.dp)
            ) {
                Row(
                ) {
                    val avatarModifier = Modifier
                        .padding(end = 8.dp)
                        .size(62.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.blue_500),
                            shape = CircleShape
                        )
                    AsyncImage(
                        model = AliUtil.nameToUrl(userViewModel.user.avatarFilename),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        contentDescription = null,
                        modifier = avatarModifier.clickable {
                            userViewModel.showChangeUserAvatarDialog = true
                        }
                    )
                    var editUsername by remember {
                        mutableStateOf(false)
                    }
                    if(editUsername){
                        val request = remember {
                            FocusRequester()
                        }
                        //在进入时选中所有字
                        val textState = remember {
                            mutableStateOf(TextFieldValue(
                                text = userViewModel.user.username,
                                selection = TextRange(0, userViewModel.user.username.length)
                            ))
                        }
                        BasicTextField(
                            value = textState.value,
                            onValueChange = {
                               if(it.text.length <= 10){
                                   textState.value = it
                               }
                            },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if(textState.value.text.trim().isNotEmpty()){
                                        userViewModel.updateUsername(textState.value.text)
                                    }else{
                                        userViewModel.showMsg("不能为空")
                                    }
                                    editUsername = false
                                }
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .focusRequester(request)
                                .width(150.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.LightGray)
                                .padding(6.dp)
                        )

                        SideEffect {
                            request.requestFocus() //请求焦点，重组中不能用
                        }
                    }else{
                        Row(
                            modifier = Modifier.align(Alignment.Bottom),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = userViewModel.user.username,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        if(userViewModel.usernameUpdating){
                                            userViewModel.showMsg("正在修改中哦")
                                        }else{
                                            editUsername = true
                                        }
                                    }
                            )
                        }
                    }
                }
                Button(onClick = { userViewModel.logout() }) {
                    Text(text = "退出登录")
                }
            }
            if(!userViewModel.user.isSeller && !userViewModel.user.isDeliveryMan)
            Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                Text(text = "我的订单")
                Row {
                    Button(onClick = {
                        navController.navigate(Screen.MineHistoryOrdersPage.route)
                    }) {
                        Text(text = "历史")
                    }
                    Button(onClick = {
                        navController.navigate(Screen.MineDeliveringOrdersPage.route)
                    }) {
                        Text(text = "配送中")
                    }
                }
            }
        }
    }
}
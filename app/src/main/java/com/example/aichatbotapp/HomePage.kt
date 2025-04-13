package com.example.aichatbotapp

import VoiceInput
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aichatbotapp.data.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(mainViewModel: MainViewModel = viewModel()) {
    var message = mainViewModel.message
    val messageList = mainViewModel.messageList
    val geminiMessageList = mainViewModel.geminiMessageList
    val context = LocalContext.current
    var showVoiceInput by remember { mutableStateOf(false) }

    if (showVoiceInput) {
        MicPermissionWrapper( onGranted = {
            VoiceInput(onResult = {
                mainViewModel.onMessageChanged(it)
                mainViewModel.sendFullMessage()
                showVoiceInput = false
            })
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat",
                    color = Color.White
                )},
                colors = TopAppBarDefaults.topAppBarColors(Color.Blue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(messageList.size) { index ->
                    MessageItem(message = Message(text = messageList[index]))
                    if (index < geminiMessageList.size) {
                        MessageItemAi(message = Message(text = geminiMessageList[index], isFromUser = false))
                    }
                }
            }

            OutlinedTextField(
                value = message,
                onValueChange = { mainViewModel.onMessageChanged(it) },
                label = { Text(text = "Enter Your Question") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        if(message.isNotEmpty()){
                            mainViewModel.sendFullMessage()
                        }else{
                            Toast.makeText(context, "Please enter a message", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                    }
                },
                leadingIcon = {
                    IconButton(onClick = {
                        showVoiceInput = true
                    }) {
                        Image(painter = painterResource(id = R.drawable.baseline_mic_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}
@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (message.isFromUser) Arrangement.Start else Arrangement.End
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.Cyan),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
@Composable
fun MessageItemAi(message: Message){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (message.isFromUser) Arrangement.Start else Arrangement.End
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.Green),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize(),
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
@Composable
fun MicPermissionWrapper(onGranted: @Composable () -> Unit) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Microphone permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    if (permissionGranted) {
        onGranted()
    }
}
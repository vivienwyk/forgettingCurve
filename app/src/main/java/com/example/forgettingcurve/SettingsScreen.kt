package com.example.forgettingcurve

import android.content.Context
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.forgettingcurve.datastore.StoreUserName
import com.example.forgettingcurve.ui.theme.Teal100
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(){

    val context = LocalContext.current
    val isPushEnableKey = booleanPreferencesKey("isPushEnable")
    var isPushDataStoreValue = flow<Boolean>{
        context.dataStore.data.map {
            it[isPushEnableKey]
        }.collect(collector = {
            if(it != null){
                emit(it)
            }
        })
    }.collectAsState(initial = false)

    val calSyncEnableKey = booleanPreferencesKey("calSyncEnable")
    var calSyncDataStoreValue = flow<Boolean>{
        context.dataStore.data.map {
            it[calSyncEnableKey]
        }.collect(collector = {
            if(it != null){
                emit(it)
            }
        })
    }.collectAsState(initial = false)

    val scope = rememberCoroutineScope()

    val dataStore = StoreUserName(context)
    val savedName = dataStore.getName.collectAsState(initial = "")
    var username by remember { mutableStateOf("") }



    Column(
    ){
        if(calSyncDataStoreValue.value){
            Row(modifier = Modifier
                .fillMaxWidth()) {
                Image(
                    modifier = Modifier.weight(1f, fill = false)
                    .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.ic_forgetting_curve),
                    contentDescription = "forgetting curve image",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        Text(
            text = "Welcome ${savedName.value}",
            color = Color.Black,
            fontSize = 18.sp,
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Image")
            Switch(
                checked = calSyncDataStoreValue.value,
                onCheckedChange = {
                    scope.launch {
                        saveCalSyncEnable(it, context)
                    }
                })
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "User Name: ")
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)) {
            //save user name
            Button(
                //modifier = Modifier.
                onClick = {
                    //launch the class in a coroutine scope
                    scope.launch {
                        dataStore.saveName(username)
                    }
                },
            ) {
                // button text
                Text(
                    text = "Save",
                    color = Color.White,
                    fontSize = 18.sp
                ) }}
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Notifications")
                Switch(
                    checked = isPushDataStoreValue.value,
                    onCheckedChange ={
                        scope.launch {
                            saveIsPushEnable(it, context)
                        }
                    })
            }

        }
    }

suspend fun saveIsPushEnable(switchValue: Boolean, localContext: Context) {
    //Create boolean preference key
    val isPushEnable = booleanPreferencesKey("isPushEnable")
    //dataStore was created in context level as Singleton.
    localContext.dataStore.edit {
        it[isPushEnable] = switchValue
    }
}

suspend fun saveCalSyncEnable(switchValue: Boolean, localContext: Context) {
    //Create boolean preference key
    val calSyncEnable = booleanPreferencesKey("calSyncEnable")
    //dataStore was created in context level as Singleton.
    localContext.dataStore.edit {
        it[calSyncEnable] = switchValue
    }
}
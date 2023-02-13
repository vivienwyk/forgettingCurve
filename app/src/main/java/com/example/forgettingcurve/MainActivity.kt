package com.example.forgettingcurve

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.forgettingcurve.ui.theme.ForgettingCurveTheme
import androidx.navigation.compose.rememberNavController

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "LocalStore")

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp(){
    val navController = rememberNavController()
    Scaffold(bottomBar ={MyBottomNavigation(navController = navController)}) {
        Box(Modifier.padding(it)){
            NavHost(navController = navController, startDestination = List.route) {
                composable(List.route) {
                    ListScreen()
                }
                composable(Curve.route) {
                    CurveScreen()
                }
                composable(Settings.route) {
                    SettingsScreen()
                }
            }
        }
        
    }

}

@Composable
fun MyBottomNavigation(navController: NavController){
    val destinationList = listOf(
        List,
        Curve,
        Settings
    )

    val selectedIndex = rememberSaveable{
        mutableStateOf(0)
    }

    BottomNavigation{
        destinationList.forEachIndexed { index, destination ->
            BottomNavigationItem (
                label = {Text(text = destination.title)},
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = destination.title
                    )
                },
                selected = index == selectedIndex.value,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(destinationList[index].route){
                        popUpTo(List.route)
                        launchSingleTop = true
                    }
                })
        }

    }
}
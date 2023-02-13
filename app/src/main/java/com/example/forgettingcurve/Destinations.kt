package com.example.forgettingcurve

interface Destinations{
    val route: String
    val icon: Int
    val title: String
}

object List: Destinations{
    override val route = "List"
    override val icon = R.drawable.ic_list
    override val title = "List"
}

object Curve: Destinations{
    override val route = "Curve"
    override val icon = R.drawable.ic_curve
    override val title = "Curve"
}

object Settings: Destinations{
    override val route = "Settings"
    override val icon = R.drawable.ic_settings
    override val title = "Settings"
}
package ru.freedominc.sfedu.navigation

import android.os.Bundle

class NavigationHelper {
    var onRecipeSelect : (String)-> Unit = { }

    var currentRecipeArgs : Bundle? = null
}
package ru.freedominc.sfedu.navigation

import ru.freedominc.sfedu.domain.Recipe

class NavigationHelper {
    var onRecipeSelect : (Recipe)-> Unit = { }

    var currentRecipe: Recipe? = null
}
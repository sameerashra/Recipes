package com.example.recipes.ui.home.recipeslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.models.Recipe
import com.example.recipes.utils.NetworkResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RecipesListViewModel: ViewModel() {

    private val db = Firebase.firestore

    private val _recipesList = MutableLiveData<NetworkResult<List<Recipe>>>(NetworkResult.Loading())
    val recipesList: LiveData<NetworkResult<List<Recipe>>>
        get() = _recipesList

    init {
        getAllRecipes()
    }

    fun getAllRecipes() {
        viewModelScope.launch {
            _recipesList.value = NetworkResult.Loading()
            // Fetch all recipes data from database
            db.collection("recipes").get()
                .addOnSuccessListener { result ->
                    val list = result.map {
                        Recipe(
                            id = it.data["id"] as Long,
                            title = it.data["title"] as String,
                            summary = it.data["summary"] as String,
                            instructions = it.data["instructions"] as String,
                            image = it.data["image"] as String
                        )
                    }
                    _recipesList.value = NetworkResult.Success(list)
                }
                .addOnFailureListener {
                    _recipesList.value = NetworkResult.Error(null, null)
                }
        }
    }
}
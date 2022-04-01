package com.example.recipes.ui.home.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.recipes.databinding.RecipesListItemBinding
import com.example.recipes.models.Recipe

class RecipesListAdapter(
    val onClick: (recipe: Recipe) -> Unit
): RecyclerView.Adapter<RecipesListAdapter.RecipesListViewHolder>(){

    // List of all recipes
    private var recipesList: List<Recipe> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesListViewHolder {
        return RecipesListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipesListViewHolder, position: Int) {
        val recipe = recipesList.getOrNull(position)
        recipe?.let {
            holder.bind(recipe, onClick)
        }
    }

    override fun getItemCount(): Int = recipesList.size

    fun submitList(newRecipeList: List<Recipe>){
        recipesList = newRecipeList
        // Since this fun will execute on initial data load only notifyDataSetChanged
        // will not have any performance hit (DiffUtil is used otherwise)
        notifyDataSetChanged()
    }

    class RecipesListViewHolder(val binding: RecipesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(recipe: Recipe, onClick: (recipe: Recipe) -> Unit){
                // Set recipe for data binding
                binding.recipe = recipe

                // Click listener to navigate to details page
                binding.recipeListLayout.setOnClickListener {
                    onClick(recipe)
                }
                binding.executePendingBindings()
            }

            companion object{
                fun from(parent: ViewGroup): RecipesListViewHolder{
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = RecipesListItemBinding.inflate(layoutInflater, parent, false)
                    return RecipesListViewHolder(binding)
                }
            }

    }
}
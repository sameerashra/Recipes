package com.example.recipes.ui.home.recipeslist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipes.R
import com.example.recipes.databinding.FragmentRecipesListBinding
import com.example.recipes.utils.NetworkResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val recipesListViewModel: RecipesListViewModel by viewModels()
    private lateinit var adapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesListBinding.inflate(layoutInflater, container, false)
        adapter = RecipesListAdapter(
            onClick = {
                // Navigate to details page
                val action = RecipesListFragmentDirections
                    .actionRecipesListFragmentToRecipeDetailsFragment(it)
                findNavController().navigate(action)
            }
        )

        observeData()

        binding.fragmentRecipesListRv.adapter = adapter

        binding.fragmentRecipesListTryAgainButton.setOnClickListener {
            recipesListViewModel.getAllRecipes()
        }

        return binding.root
    }

    // Observe data from view model
    private fun observeData() {
        recipesListViewModel.recipesList.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Loading -> {
                    binding.fragmentRecipesListRv.isVisible = false
                    binding.fragmentRecipesListTryAgainContainer.isVisible = false
                    binding.fragmentRecipesListProgressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    binding.fragmentRecipesListRv.isVisible = true
                    binding.fragmentRecipesListTryAgainContainer.isVisible = false
                    binding.fragmentRecipesListProgressBar.isVisible = false
                    it.data?.let { recipes ->
                        adapter.submitList(recipes)
                    }
                }
                is NetworkResult.Error -> {
                    binding.fragmentRecipesListRv.isVisible = false
                    binding.fragmentRecipesListTryAgainContainer.isVisible = true
                    binding.fragmentRecipesListProgressBar.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
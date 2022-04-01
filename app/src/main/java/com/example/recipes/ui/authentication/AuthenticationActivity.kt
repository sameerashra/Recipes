package com.example.recipes.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipes.R
import com.example.recipes.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
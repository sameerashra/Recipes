package com.example.recipes.ui.authentication.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipes.R
import com.example.recipes.databinding.FragmentLoginBinding
import com.example.recipes.ui.home.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private var loadingAlertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        auth = Firebase.auth

        binding.fragmentLoginSignupTextView.setOnClickListener {
            // Navigate to Sign up page
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        binding.fragmentLoginMaterialButton.setOnClickListener {
            // Hide keyboard
            val imm =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            val email = binding.fragmentLoginEmailTextInput.editText?.text.toString()
            val password = binding.fragmentLoginPasswordTextInput.editText?.text.toString()

            // Continue if email and password are valid
            if (validateFields(email, password)) {
                presentLoadingAlert()
                // Perform Login
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Login success
                            closeLoadingAlert()
                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            closeLoadingAlert()
                            showAlert(
                                title = "Login Failed",
                                message = task.exception?.localizedMessage ?: "Something went wrong"
                            )
                        }
                    }
            }
        }
        return binding.root
    }

    private fun showAlert(title: String, message: String) {
        val alertDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Ok") { dialog, _ ->
                dialog.cancel()
            }
            setCancelable(true)
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun validateFields(email: String, password: String): Boolean {
        // Check if email is valid
        var isEmailValid = false
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.fragmentLoginEmailTextInput.error = null
            isEmailValid = true
        } else {
            binding.fragmentLoginEmailTextInput.error = "Invalid Email"
            isEmailValid = false
        }

        // Check if password is not empty
        var isPasswordValid = false
        if (password.isEmpty()) {
            isPasswordValid = false
            binding.fragmentLoginPasswordTextInput.error = "Password required"
        } else {
            isPasswordValid = true
            binding.fragmentLoginPasswordTextInput.error = null
        }

        return isEmailValid && isPasswordValid
    }

    private fun presentLoadingAlert() {
        val alertBuilder = MaterialAlertDialogBuilder(requireContext())
        alertBuilder.setView(layoutInflater.inflate(R.layout.loading_alert, null))
        alertBuilder.setCancelable(false)
        loadingAlertDialog = alertBuilder.show()
    }

    private fun closeLoadingAlert() {
        loadingAlertDialog?.let {
            if (it.isShowing) {
                it.cancel()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
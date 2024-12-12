package com.example.capstone.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.capstone.R
import com.example.capstone.Repository
import com.example.capstone.User.UserPreference
import com.example.capstone.User.dataStore
import com.example.capstone.ViewModelFactory
import com.example.capstone.api.ApiConfig
import com.example.capstone.login.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference.getInstance(requireContext().dataStore)

        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                Log.d("ProfileFragment", "User: $user")
                user.token?.let { token ->
                    Log.d("ProfileFragment", "Token from UserPreference: $token")

                    val authApi = ApiConfig.getDefaultApiService(token)
                    val imageApi = ApiConfig.getProcessImageApiService(token)

                    val repository = Repository.getInstance(userPreference, authApi, imageApi)

                    viewModel = ViewModelProvider(
                        this@ProfileFragment,
                        ViewModelFactory(repository)
                    )[ProfileViewModel::class.java]

                    viewModel.loadUserProfile(token)

                    viewModel.userProfile.collect { userProfile ->
                        Log.d("ProfileFragment", "UserProfile: $userProfile")
                        userProfile?.let {
                            tvEmail.text = "Email: ${it.email}"
                            tvName.text = "Name: ${it.name}"
                        }
                    }
                }
            }
        }

        btnLogout.setOnClickListener {
            lifecycleScope.launch {
                userPreference.logout()
                moveToLogin()
            }
        }
    }

    private fun moveToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}




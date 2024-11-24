package com.example.capstone.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.capstone.R
import com.example.capstone.User.UserPreference
import com.example.capstone.User.dataStore
import com.example.capstone.login.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi UserPreference
        userPreference = UserPreference.getInstance(requireContext().dataStore)

        val tvEmail = view.findViewById<TextView>(R.id.etEmail)
        val tvName = view.findViewById<TextView>(R.id.etName)
        val tvPassword = view.findViewById<TextView>(R.id.etPassword)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // Ambil data user dari DataStore dan tampilkan
        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                tvEmail.text = "Email : ${user.email}"
            }
        }

        // Tombol Logout
        btnLogout.setOnClickListener {
            lifecycleScope.launch {
                userPreference.logout() // Hapus session
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

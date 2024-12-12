package com.example.capstone.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.capstone.R
import com.example.capstone.ViewModelFactory
import com.example.capstone.databinding.ActivityRegisterBinding
import com.example.capstone.login.LoginActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isPasswordVisible = false

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setupView()
        observeViewModel()
        actionListener()
        playAnimation()
        setupPasswordVisibilityToggle()
    }

    private fun setupPasswordVisibilityToggle() {
        val passwordEditText = binding.edRegisterPassword
        val passwordToggle = binding.passwordVisibilityToggle

        passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_visibility_24)
            } else {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_visibility_off_24)
            }
            passwordEditText.setSelection(passwordEditText.text?.length ?: 0)

        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun actionListener() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            var valid = true
            when {
                name.isEmpty() -> binding.edRegisterName.setErrorIfEmpty(getString(R.string.field_empty))
                    .also { valid = false }

                email.isEmpty() -> binding.edRegisterEmail.setErrorIfEmpty(getString(R.string.field_empty))
                    .also { valid = false }

                password.isEmpty() -> binding.edRegisterPassword.setErrorIfEmpty(getString(R.string.field_empty))
                    .also { valid = false }

                password.length < 8 -> {
                    binding.edRegisterPassword.error = "Password harus berisi minimal 8 karakter"
                    valid = false
                }
            }

            val emailPattern = Patterns.EMAIL_ADDRESS
            if (email.isEmpty()) {
                binding.edRegisterEmail.setErrorIfEmpty(getString(R.string.field_empty))
                valid = false
            } else if (!emailPattern.matcher(email).matches()) {
                binding.edRegisterEmail.error = "Format email tidak valid"
                valid = false
            }

            if (valid) {
                viewModel.userRegister(name, email, password)
            }
        }
    }

    private fun EditText.setErrorIfEmpty(message: String) {
        if (text.isEmpty()) {
            error = message
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Sukses")
            setMessage("Registrasi Berhasil")
            setPositiveButton("Lanjut") { _, _ ->
                val mainIntent = Intent(context, LoginActivity::class.java)
                mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(mainIntent)
                finish()
            }
            create()
            show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.registerState.collect { state ->
                when (state) {
                    is RegisterState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is RegisterState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(state.response.message ?: "Registrasi berhasil")
                        showDialog()
                    }
                    is RegisterState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(state.message)
                    }
                    RegisterState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        val cardViewAnim = ObjectAnimator.ofFloat(binding.cardView, "alpha", 0f, 1f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val buttonAnim = ObjectAnimator.ofFloat(binding.btnRegister, "translationY", 100f, 0f).apply {
            duration = 1000
            interpolator = OvershootInterpolator()
        }

        val textAnim = ObjectAnimator.ofFloat(binding.hiWelcome, "translationX", -200f, 0f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val animSet = AnimatorSet().apply {
            playTogether(cardViewAnim, buttonAnim, textAnim)
        }

        animSet.start()
    }
}

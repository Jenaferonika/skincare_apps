package com.example.capstone.result

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstone.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val benefitsList = intent.getStringArrayListExtra("benefits_list") ?: arrayListOf()
        val ingredientsList = intent.getStringArrayListExtra("ingredients_list") ?: arrayListOf()
        val functionList = intent.getStringArrayListExtra("function_list") ?: arrayListOf()
        val ratingList = intent.getStringArrayListExtra("rating_list") ?: arrayListOf()

        if (ingredientsList.isNotEmpty() && functionList.isNotEmpty() && ratingList.isNotEmpty() && ingredientsList.size == functionList.size && ingredientsList.size == ratingList.size) {

            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            binding.recyclerView.layoutManager = LinearLayoutManager(this)

            val adapter = IngredientsAdapter(ingredientsList, functionList, ratingList, benefitsList)
            binding.recyclerView.adapter = adapter
        } else {
            // Tampilkan empty view jika data tidak valid
            binding.recyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        }

        onBackPressedDispatcher.addCallback(this) {
            navigateBackToHome()
        }
    }

    private fun navigateBackToHome() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navigateBackToHome()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
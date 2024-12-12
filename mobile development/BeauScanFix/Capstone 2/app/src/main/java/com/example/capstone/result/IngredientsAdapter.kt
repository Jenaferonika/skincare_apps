package com.example.capstone.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.databinding.ItemBenefitsBinding
import com.example.capstone.databinding.ItemIngredientsBinding

private const val TYPE_HEADER = 0
private const val TYPE_ITEM = 1

class IngredientsAdapter(
    private val ingredients: List<String>,
    private val functions: List<String>,
    private val ratings: List<String>,
    private val benefits: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: String, function: String, rating: String) {
            binding.tvItemName.text = "Ingredient: $ingredient"
            binding.tvItemDescription.text = "Function: $function"
            binding.tvCategories.text = "Rating: $rating"
        }
    }

    class HeaderViewHolder(private val binding: ItemBenefitsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(benefits: String) {
            binding.tvBenefits.text = benefits
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = ItemBenefitsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            TYPE_ITEM -> {
                val binding = ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(benefits.joinToString("\n"))
            }
            is ViewHolder -> {
                val itemPosition = position - 1 // Adjust position for items after header
                holder.bind(ingredients[itemPosition], functions[itemPosition], ratings[itemPosition])
            }
        }
    }

    override fun getItemCount(): Int = ingredients.size + 1
}


package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foody.databinding.IngredientsRowLayoutBinding
import com.example.foody.model.ExtendedIngredient
import com.example.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foody.util.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data= ingredientsList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }



    class MyViewHolder(private val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result:ExtendedIngredient){
            binding.ingredientImageview.load(BASE_IMAGE_URL+ result.image){
                crossfade(600)
            }
            binding.ingredientName.text = result.name.capitalize()
            binding.ingredientAmount.text = result.amount.toString()
            binding.ingredientUnit.text = result.unit
            binding.ingredientConsistency.text = result.consistency
            binding.ingredientOriginal.text = result.original


        }


        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    fun setData(newIngredients:List<ExtendedIngredient>){
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList,newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList= newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
        
    }
}
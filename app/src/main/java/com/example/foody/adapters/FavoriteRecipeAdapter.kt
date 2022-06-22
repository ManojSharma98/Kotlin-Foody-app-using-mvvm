package com.example.foody.adapters

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.databinding.FavoriteRecipeRowLayoutBinding
import com.example.foody.fragments.FavoriteRecipesFragmentDirections
import com.example.foody.util.RecipesDiffUtil
import com.example.foody.viewModels.MainViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

class FavoriteRecipeAdapter(private val requireActivity: FragmentActivity, private val mainViewModel: MainViewModel ) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favoriteRecipes = emptyList<FavoritesEntity>()
    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var myviewholders = arrayListOf<MyViewHolder>()
    private lateinit var mActionMode :ActionMode
    private lateinit var rootView:View


    class MyViewHolder(private val binding: FavoriteRecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings() // update all our views
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myviewholders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)
        val cardLayout =
            holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteRecipesRowLayout)
        cardLayout.setOnClickListener {

            if (multiSelection){
                applySelection(holder,currentRecipe)
            }
            else{
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }

        }


        cardLayout.setOnLongClickListener {
            if (!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder,currentRecipe)
                true
            }
            else{
                multiSelection = false
                false
            }

        }


    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
            applyActionModelTitle()

        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor,R.color.purple_500)
            applyActionModelTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        val constarintLayout =
            holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteRecipesRowLayout)
        constarintLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
    }

    private fun applyActionModelTitle(){
        when(selectedRecipes.size){
            0->mActionMode.finish()
            1->mActionMode.title = "${selectedRecipes.size} item selected"
            else->mActionMode.title = "items selected"
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriterecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriterecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(actionmode: ActionMode?, menu: Menu?): Boolean {
        actionmode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = actionmode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionmode: ActionMode?, menu: Menu?): Boolean {
        return true

    }

    override fun onActionItemClicked(actionmode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_recipe_menu){
            selectedRecipes.forEach{
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} recipe/s removed.")
            multiSelection=false
            selectedRecipes.clear()
            actionmode?.finish()
        }
        return true

    }
    private fun showSnackBar(message:String){
        Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).setAction("Okay"){}
            .show()
    }

    override fun onDestroyActionMode(actionmode: ActionMode?) {
        myviewholders.forEach{holder->
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
            }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)

    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}
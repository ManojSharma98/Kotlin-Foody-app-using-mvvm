package com.example.foody.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.fragments.IngredientsFragment
import com.example.foody.fragments.InstructionFragment
import com.example.foody.fragments.OverviewFragment
import com.example.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foody.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()

    private val mainViewModel:MainViewModel by viewModels()
    private var recipeSaved = false
    private var savedRecipeId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)


        val adapter = PagerAdapter(
            resultBundle, fragments, titles, supportFragmentManager
        )

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoriteEntity ->
            try {
                    for (savedRecipe in favoriteEntity){
                        if (savedRecipe.result.id == args.result.id){
                            changeMenuItemColor(menuItem,R.color.yellow)
                            savedRecipeId = savedRecipe.id
                            recipeSaved = true
                        }
                        else{
                            changeMenuItemColor(menuItem,R.color.white)
                        }
                    }
            }catch (e:Exception){
                Log.e("DetailsActivity",e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        else if (item.itemId==R.id.save_to_favorites_menu && !recipeSaved){
            saveToFavorite(item)
        }
        else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved){
            removeFromFavorites(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorite(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(0,args.result)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.yellow)
        showSnackBar("Recipe saved")
        recipeSaved= true
    }

    private fun removeFromFavorites(item:MenuItem){
        val favoritesEntity = FavoritesEntity(
            savedRecipeId,args.result
        )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item,R.color.white)
        showSnackBar("Removed form favorites")
        recipeSaved= false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.detailsLayout,message,Snackbar.LENGTH_SHORT).setAction("Okay"){}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, yellow: Int) {
    item.icon.setTint(ContextCompat.getColor(this,yellow))
    }
}
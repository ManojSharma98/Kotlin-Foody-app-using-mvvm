package com.example.foody.data

import com.example.foody.data.database.entities.RecipeEntity
import com.example.foody.data.database.RecipesDao
import com.example.foody.data.database.entities.FavoritesEntity
import com.example.foody.data.database.entities.FoodJokeEntity
import com.example.foody.model.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

     fun readRecipes(): Flow<List<RecipeEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes():Flow<List<FavoritesEntity>>{
        return recipesDao.readFavoriteRecipes()
    }


    suspend fun insertRecipes(recipesEntity: RecipeEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipe(favoritesEntity)

    }

    suspend fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }


    fun readFoodJoke ():Flow<List<FoodJokeEntity>>{
        return recipesDao.readFoodJoke()
    }
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }



}
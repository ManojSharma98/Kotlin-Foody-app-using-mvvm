package com.example.foody.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.foody.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.foody.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.foody.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.foody.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.foody.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = PREFERENCES_NAME)

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.selectedMealType] = mealType
            preference[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preference[PreferenceKeys.selectedDietType] = dietType
            preference[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline:Boolean){
        dataStore.edit { prefreances->
            prefreances[PreferenceKeys.backOnline] = backOnline

        }
    }



    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }

    }
        .map { preference ->
            val selectedMealType = preference[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preference[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preference[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preference[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType, selectedMealTypeId, selectedDietType, selectedDietTypeId
            )
        }

    val readBackOnline:Flow<Boolean> = dataStore.data
        .catch { exception->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
             preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline]?:false
            backOnline
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)
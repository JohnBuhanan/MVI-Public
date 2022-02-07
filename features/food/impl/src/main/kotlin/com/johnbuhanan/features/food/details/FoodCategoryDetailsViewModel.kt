package com.johnbuhanan.features.food.details

import androidx.lifecycle.viewModelScope
import com.johnbuhanan.common.BaseViewModel
import com.johnbuhanan.features.food.details.FoodCategoryDetailsEvent.TappedBack
import com.johnbuhanan.features.food.details.FoodCategoryDetailsEvent.TappedFoodItem
import com.johnbuhanan.features.food.domain.FoodMenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FoodCategoryDetailsViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val repository: FoodMenuRepository,
) : BaseViewModel<FoodCategoryDetailsEvent, FoodCategoryDetailsState, FoodCategoryDetailsEffect>(dispatcher) {

    fun initialize(categoryId: String) {
        Timber.e("launchFoodItems")

        viewModelScope.launch {
            val categories = repository.getFoodCategories()
            val category = categories.first { it.id == categoryId }
            setState { copy(category = category) }

            val foodItems = repository.getMealsByCategory(categoryId)
            setState { copy(categoryFoodItems = foodItems) }
        }
    }

    override fun setInitialState() = FoodCategoryDetailsState(null, listOf())

    override fun handleEvents(event: FoodCategoryDetailsEvent) {
        Timber.e("handleEvents")
        when (event) {
            TappedBack -> {}//router.goBack()
            is TappedFoodItem -> {}//router.goToFeature(FeatureRoute.FeatureA)
        }
    }

    companion object {
        const val CATEGORY_ID = "categoryId"
    }
}
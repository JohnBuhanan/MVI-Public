package com.johnbuhanan.features.food.domain

import javax.inject.Inject
import javax.inject.Singleton

interface FoodMenuRepository {
    suspend fun getFoodCategories(): List<FoodItem>
    suspend fun getMealsByCategory(categoryId: String): List<FoodItem>
}

@Singleton
class FoodMenuRepositoryImpl @Inject constructor(private val foodMenuApi: FoodMenuApi) : FoodMenuRepository {
    private var cachedCategories: List<FoodItem>? = null

    override suspend fun getFoodCategories(): List<FoodItem> {
        var cachedCategories = cachedCategories
        if (cachedCategories == null) {
            cachedCategories = foodMenuApi.getFoodCategories().mapCategoriesToItems()
            this.cachedCategories = cachedCategories
        }
        return cachedCategories
    }

    override suspend fun getMealsByCategory(categoryId: String): List<FoodItem> {
        val categoryName = getFoodCategories().first { it.id == categoryId }.name
        return foodMenuApi.getMealsByCategory(categoryName).mapMealsToItems()
    }


    private fun FoodCategoriesResponse.mapCategoriesToItems(): List<FoodItem> {
        return this.categories.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                description = category.description,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }

    private fun MealsResponse.mapMealsToItems(): List<FoodItem> {
        return this.meals.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }
}

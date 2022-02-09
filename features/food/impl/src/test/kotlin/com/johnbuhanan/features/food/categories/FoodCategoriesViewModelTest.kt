package com.johnbuhanan.features.food.categories

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.johnbuhanan.features.food.Food
import com.johnbuhanan.features.food.domain.FoodItem
import com.johnbuhanan.features.food.domain.FoodMenuRepository
import com.johnbuhanan.navigation.RouterEvent
import com.johnbuhanan.navigation.RouterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class FoodCategoriesViewModelTest {
    private val repository = object : FoodMenuRepository {
        override suspend fun getFoodCategories(): List<FoodItem> = listOf()
        override suspend fun getMealsByCategory(categoryId: String): List<FoodItem> = listOf()
    }

    private val router = RouterImpl()

    private val dispatcher = StandardTestDispatcher()
    private val foodCategoriesViewModel = FoodCategoriesViewModel(
        mainDispatcher = dispatcher,
        ioDispatcher = dispatcher,
        repository = repository,
        router = router,
    )

    @Before
    fun setup() {
        // Subsequently-called runTest,
        // as well as TestScope and test dispatcher constructors,
        // will use the TestCoroutineScheduler of the provided dispatcher.
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `when TappedBack then emit Pop`() = runTest {
        router.routerEvents.test {
            foodCategoriesViewModel.setEvent(FoodCategoriesEvent.TappedBack)
            assertThat(awaitItem()).isEqualTo(RouterEvent.Pop)
        }
    }

    @Test
    fun `when TappedCategory then emit Push`() = runTest {
        router.routerEvents.test {
            foodCategoriesViewModel.setEvent(FoodCategoriesEvent.TappedCategory("1"))
            val routerEvent = awaitItem()
            assertThat(routerEvent).isInstanceOf(RouterEvent.Push::class)

            val route = (routerEvent as RouterEvent.Push).routes.first()

            assertThat(route).isEqualTo(Food.Route.FoodCategoryDetails("1"))
        }
    }
}
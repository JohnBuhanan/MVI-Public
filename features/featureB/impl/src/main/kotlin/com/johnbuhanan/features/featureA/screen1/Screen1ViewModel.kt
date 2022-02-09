package com.johnbuhanan.features.featureB.screen1

import com.johnbuhanan.common.BaseViewModel
import com.johnbuhanan.features.featureB.api.FeatureB
import com.johnbuhanan.navigation.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class Screen1ViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val router: Router,
) : BaseViewModel<Screen1Event, Screen1State, Screen1Effect>(dispatcher) {

    init {
        setState { copy(message = "FeatureB - Screen1") }
    }

    override fun setInitialState() = Screen1State("")

    override fun handleEvents(event: Screen1Event) {
        Timber.e("handleEvents")
        when (event) {
            is Screen1Event.TappedNext -> {
                router.push(FeatureB.Route.Screen2)
            }
        }
    }
}

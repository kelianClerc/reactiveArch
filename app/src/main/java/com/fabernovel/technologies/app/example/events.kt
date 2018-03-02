package com.fabernovel.technologies.app.example

sealed class ExampleUiEvent {
    object Refresh : ExampleUiEvent()
    data class Detail(val id: String) : ExampleUiEvent()
}

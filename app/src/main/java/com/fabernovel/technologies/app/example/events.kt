package com.fabernovel.technologies.app.example

sealed class ExampleUiEvent {
    object Refresh : ExampleUiEvent()
    object GoToUsers: ExampleUiEvent()
    data class Detail(val id: String) : ExampleUiEvent()
}

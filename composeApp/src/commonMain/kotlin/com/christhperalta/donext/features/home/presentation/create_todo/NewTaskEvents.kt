package com.christhperalta.donext.features.home.presentation.create_todo



sealed class NewTaskEvents {
    data class OnDescriptionChange(val description: String) : NewTaskEvents()
    data object OnCreateTask : NewTaskEvents()
}

package com.example.todolist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodoViewModel : ViewModel() {
    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    private val _newTodoText = MutableStateFlow("")
    val newTodoText: StateFlow<String> = _newTodoText

    fun onFabClick() {
        _showBottomSheet.value = true
    }

    fun onSaveClick() {
        if (_newTodoText.value.isNotBlank()) {
            val newItem = TodoItem(_newTodoText.value)
            _todoItems.value = _todoItems.value + newItem
            _newTodoText.value = ""
            _showBottomSheet.value = false
        }
    }

    fun onCancelClick() {
        _newTodoText.value = ""
        _showBottomSheet.value = false
    }

    fun onClearClick() {
        _newTodoText.value = ""
    }

    fun onItemCheckedChange(item: TodoItem, isChecked: Boolean) {
        item.isCompleted = isChecked
        _todoItems.value = _todoItems.value.toMutableList() // Trigger recomposition
    }

    fun onTextChange(newText: String) {
        _newTodoText.value = newText
    }
}

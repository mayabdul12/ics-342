package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val viewModel: TodoViewModel = viewModel()
    val todoItems by viewModel.todoItems.collectAsState()
    val showBottomSheet by viewModel.showBottomSheet.collectAsState()
    val newTodoText by viewModel.newTodoText.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Todo List") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onFabClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) {
        Column(Modifier.padding(it)) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(todoItems) { item ->
                    TodoRow(item, viewModel::onItemCheckedChange)
                }
            }
            if (showBottomSheet) {
                BottomSheet(
                    newTodoText = newTodoText,
                    onSave = viewModel::onSaveClick,
                    onCancel = viewModel::onCancelClick,
                    onClear = viewModel::onClearClick
                )
            }
        }
    }
}

@Composable
fun TodoRow(item: TodoItem, onCheckedChange: (TodoItem, Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.text, fontSize = 18.sp)
        Checkbox(checked = item.isCompleted, onCheckedChange = { onCheckedChange(item, it) })
    }
}

@Composable
fun BottomSheet(
    newTodoText: String,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onClear: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = newTodoText,
            onValueChange = { /* Update the value in ViewModel */ },
            label = { Text("New Todo") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onSave) {
                Text("Save")
            }
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
            IconButton(onClick = onClear) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
            }
        }
    }
}

data class TodoItem(val text: String, var isCompleted: Boolean = false)

package com.example.notepad.presentation.navigation

import CreateNoteScreen
import EditNoteScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.notepad.presentation.screens.notes.NotesScreen

@Composable
fun NavGraph() {
    val screen = remember { mutableStateOf<Screen>(Screen.Notes) }

    val currentScreen = screen.value
    when (currentScreen) {
        Screen.CreateNote -> {
            CreateNoteScreen(onFinished = {
                screen.value = Screen.Notes
            })
        }

        is Screen.EditNote -> {
            EditNoteScreen(
                noteId = currentScreen.noteId,
                onFinished = {
                    screen.value = Screen.Notes
                }
            )
        }

        Screen.Notes -> {
            NotesScreen(
                modifier = Modifier,
                onNoteClick = {
                    screen.value = Screen.EditNote(it.id)
                },
                onFloatingButtonClick = {
                    screen.value = Screen.CreateNote
                })
        }
    }
}

sealed interface Screen {
    data object Notes : Screen
    data object CreateNote : Screen
    data class EditNote(val noteId: Int) : Screen
}
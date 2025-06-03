package com.example.notepad.presentation.navigation

import CreateNoteScreen
import EditNoteScreen
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notepad.presentation.navigation.Screen.EditNote.getNoteId
import com.example.notepad.presentation.screens.notes.NotesScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route
    ) {
        composable(Screen.Notes.route) {
            NotesScreen(onNoteClick = {
                navController.navigate(Screen.EditNote.createRout(it.id)) // edit_note/5
            }, onFloatingButtonClick = {
                navController.navigate(Screen.CreateNote.route)
            }
            )
        }
        composable(Screen.CreateNote.route) {
            CreateNoteScreen(onFinished = {
                navController.popBackStack()
            })
        }
        composable(Screen.EditNote.route) {
            EditNoteScreen(noteId = getNoteId(it.arguments), onFinished = {
                navController.popBackStack()
            })
        }
    }
}

sealed class Screen(val route: String) {
    data object Notes : Screen("notes")
    data object CreateNote : Screen("create note")
    data object EditNote : Screen("edit note/{note id}") {
        fun createRout(noteId: Int): String {
            return "edit note/${noteId}"
        }

        fun getNoteId(arguments: Bundle?): Int {
            return arguments?.getString("note id")?.toInt() ?: 0
        }
    }
}
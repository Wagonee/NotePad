package com.example.notepad.presentation

import CreateNoteScreen
import EditNoteScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.example.notepad.presentation.navigation.NavGraph
import com.example.notepad.presentation.screens.notes.NotesScreen
import com.example.notepad.presentation.ui.theme.NotePadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotePadTheme {
                NotesScreen(
                    onNoteClick = {

                    },
                    onFloatingButtonClick = {

                    })
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}


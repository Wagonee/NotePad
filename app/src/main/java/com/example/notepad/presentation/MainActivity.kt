package com.example.notepad.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notepad.presentation.navigation.NavGraph
import com.example.notepad.presentation.screens.notes.NotesScreen
import com.example.notepad.presentation.ui.theme.NotePadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotePadTheme {
                NavGraph()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}


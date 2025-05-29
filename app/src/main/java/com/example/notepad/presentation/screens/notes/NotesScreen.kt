package com.example.notepad.presentation.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notepad.domain.Note

@Composable
fun NotesScreen(
    modifier: Modifier,
    viewModel: NotesViewModel = viewModel()
) {
    val currentState by viewModel.state.collectAsState()

    // val currentState = state.value
    // val scrollState = remember { ScrollState(0) }

    LazyColumn(
        modifier = Modifier
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            LazyRow() {
                items(currentState.pinnedNotes) { note ->
                    NotesCard(
                        modifier = Modifier,
                        note = note,
                        onNoteClick = {
                            viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                        }
                    )
                }
            }
        }
        items(currentState.otherNotes) { note ->
            NotesCard(
                modifier = Modifier,
                note = note,
                onNoteClick = {
                    viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                }
            )
        }

    }
}

@Composable
fun NotesCard(
    modifier: Modifier,
    note: Note,
    onNoteClick: (Note) -> Unit
) {
    Text(
        modifier = Modifier.clickable {
            onNoteClick(note)
        },
        text = "${note.title} - ${note.content}",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}
package com.example.notepad.presentation.screens.notes


import android.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notepad.domain.Note
import com.example.notepad.presentation.ui.theme.OtherNotesColors
import com.example.notepad.presentation.ui.theme.PinnedNotesColors
import com.example.notepad.presentation.utils.DateFormatter

@Composable
fun NotesScreen(
    modifier: Modifier,
    viewModel: NotesViewModel = viewModel(),
    onNoteClick: (Note) -> Unit,
    onFloatingButtonClick: () -> Unit
) {
    val currentState by viewModel.state.collectAsState()
    val notesIsAdded = currentState.pinnedNotes.isNotEmpty() && currentState.otherNotes.isNotEmpty()
    Scaffold(
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFloatingButtonClick()
                },
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Button for adding note"
                )
            }
        }) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            item {
                Title(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "All Notes"
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    query = currentState.query,
                ) { viewModel.processCommand(NotesCommand.InputSearchQuery(it)) }
                if (currentState.pinnedNotes.isEmpty() && currentState.otherNotes.isEmpty() && currentState.query.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(96.dp))
                    MessageToAdd(modifier = Modifier.padding(horizontal = 48.dp),text = "Add note before search.")
                }
            }

            if (notesIsAdded) {

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Subtitle(
                        modifier = Modifier.padding(horizontal = 24.dp), text = "Pinned"
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(currentState.pinnedNotes, { _, note -> note.id }) { index, note ->
                        NoteCard(
                            modifier = Modifier.widthIn(max = 160.dp),
                            note = note,
                            onNoteClick = {
                                onNoteClick
                            },
                            onLongClick = {
                                viewModel.processCommand(
                                    NotesCommand.SwitchPinnedStatus(
                                        it.id
                                    )
                                )
                            },
                            backgroundColor = PinnedNotesColors[index % PinnedNotesColors.size]
                        )
                    }
                }
            }
            if (notesIsAdded) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item { Subtitle(modifier = Modifier.padding(horizontal = 24.dp), text = "Others") }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else if (currentState.query.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                    MessageToAdd(modifier = Modifier.padding(horizontal = 48.dp),text = "Add your first note!")
                }
            }

            itemsIndexed(currentState.otherNotes, { _, note -> note.id }) { index, note ->
                NoteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    note = note,

                    onNoteClick = {
                        onNoteClick(note)
                    },
                    onLongClick = { viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id)) },
                    backgroundColor = OtherNotesColors[index % OtherNotesColors.size]
                )
                Spacer(modifier = Modifier.height(8.dp))
            }


        }
    }
    // val currentState = state.value
    // val scrollState = remember { ScrollState(0) }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    backgroundColor: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                },
                onLongClick = {
                    onLongClick(note)
                }
            )
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            fontSize = 14.sp,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = DateFormatter.formatDateToString(note.updatedAt),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = note.content,
            maxLines = 3,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )

    }

}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(10.dp)
            ),

        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search bar.",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun Subtitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
}

@Composable
private fun MessageToAdd(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

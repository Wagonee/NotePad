package com.example.notepad.presentation.screens.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(),
    onNoteClick: (Note) -> Unit,
    onFloatingButtonClick: () -> Unit
) {
    val currentState by viewModel.state.collectAsState()

    val hasAnyNotes = currentState.pinnedNotes.isNotEmpty() || currentState.otherNotes.isNotEmpty()
    val isSearching = currentState.query.isNotEmpty()

    val pinnedFiltered = currentState.pinnedNotes
    val otherFiltered = currentState.otherNotes

    val searchReturnedNothing = isSearching && pinnedFiltered.isEmpty() && otherFiltered.isEmpty()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFloatingButtonClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new note"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!hasAnyNotes && !isSearching) {
                EmptyStateMessage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    text = "The notes list is empty.\nAdd one!"
                )
                return@Scaffold
            }

            if (searchReturnedNothing) {
                EmptyStateMessage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    text = "For query: '${currentState.query}' nothing find.\nPlease, try another."
                )
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        text = "All Notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        value = currentState.query,
                        onValueChange = { viewModel.processCommand(NotesCommand.InputSearchQuery(it)) },
                        placeholder = {
                            Text(
                                text = "Search...",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search note",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                if (pinnedFiltered.isNotEmpty()) {
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                    item {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth(),
                            text = "Pinned",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(
                                items = pinnedFiltered,
                                key = { _, note -> note.id }
                            ) { index, note ->
                                NoteCard(
                                    note = note,
                                    backgroundColor = PinnedNotesColors[index % PinnedNotesColors.size],
                                    onNoteClick = onNoteClick,
                                    onLongClick = {
                                        viewModel.processCommand(
                                            NotesCommand.SwitchPinnedStatus(
                                                it.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                if (otherFiltered.isNotEmpty()) {
                    item { Spacer(modifier = Modifier.height(24.dp)) }

                    if (pinnedFiltered.isNotEmpty()) {
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .fillMaxWidth(),
                                text = "Others",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }

                    itemsIndexed(
                        items = otherFiltered,
                        key = { _, note -> note.id }
                    ) { index, note ->
                        NoteCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            note = note,
                            backgroundColor = OtherNotesColors[index % OtherNotesColors.size],
                            onNoteClick = onNoteClick,
                            onLongClick = {
                                viewModel.processCommand(
                                    NotesCommand.SwitchPinnedStatus(
                                        it.id
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
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
                onClick = { onNoteClick(note) },
                onLongClick = { onLongClick(note) }
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
private fun EmptyStateMessage(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

package com.example.notepad.data

import com.example.notepad.domain.Note
import com.example.notepad.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object TestNotesRepositoryImpl : NotesRepository {

    private val notesListFlow = MutableStateFlow<List<Note>>(listOf())

    override fun addNote(title: String, content: String) {
//        val newNotes = notesListFlow.value.toMutableList()
//        newNotes.add(note)
//        notesListFlow.value = newNotes
        notesListFlow.update {
//            it.toMutableList().apply {
            oldList ->
            val note = Note(
                id = oldList.size,
                title = title,
                content = content,
                updatedAt = System.currentTimeMillis(),
                isPinned = false
            )
            oldList + note
        }
    }

    override fun deleteNote(id: Int) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().apply {
                removeIf {
                    it.id == id
                }
            }
        }
    }

    override fun editNote(note: Note) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().map {
                if (it.id == note.id) {
                    note
                } else {
                    it
                }
            }
        }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesListFlow.asStateFlow()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesListFlow.map { currentList ->
            currentList.filter {
                it.title.contains(query) || it.content.contains(query)
            }
        }
    }

    override fun getNote(id: Int): Note {
        return notesListFlow.value.first { it.id == id }
    }

    override fun switchPinnedStatus(id: Int) {
        notesListFlow.update { oldList ->
            oldList.toMutableList().map {
                if (it.id == id) {
                    it.copy(isPinned = !it.isPinned)
                } else {
                    it
                }
            }
        }
    }
}
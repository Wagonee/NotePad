package com.example.notepad.data

import android.content.Context
import com.example.notepad.domain.Note
import com.example.notepad.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl private constructor(context: Context) : NotesRepository {
    private val notesDatabase = NotesDatabase.getInstance(context)
    private val notesDao = notesDatabase.notesDao()
    override suspend fun addNote(
        title: String,
        content: String,
        isPinned: Boolean,
        updatedAt: Long
    ) {
        val noteDbModel = NoteDbModel(0, title, content, updatedAt, isPinned)
        notesDao.addNote(noteDbModel)
    }

    override suspend fun deleteNote(id: Int) {
        notesDao.deleteNote(id)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toDbModel())
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {
            it.map {
                it.toEntity()
            }
        }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map {
            it.map {
                it.toEntity()
            }
        }
    }

    override suspend fun getNote(id: Int): Note {
        return notesDao.getNote(id).toEntity()
    }

    override suspend fun switchPinnedStatus(id: Int) {
        notesDao.switchPinnedStatus(id)
    }

    companion object {
        private val LOCK = Any()
        private var instance: NotesRepositoryImpl? = null
        fun getInstance(context: Context): NotesRepositoryImpl {
            synchronized(LOCK) {
                instance?.let { return it }
                return NotesRepositoryImpl(context).also { instance = it }
            }
        }
    }
}
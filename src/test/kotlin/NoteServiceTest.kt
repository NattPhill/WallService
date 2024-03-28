import org.junit.Before
import org.junit.Test
import ru.netology.*
import org.junit.Assert.*
import ru.netology.Comment
import ru.netology.Note
import ru.netology.NoteService

class NoteServiceTest {
    lateinit var noteService: NoteService<Note>
    lateinit var note1: Note
    lateinit var note2: Note
    @Before
    fun setUp() {
        noteService = NoteService()
        note1 = Note(1, "Title 1", "Text 1")
        note2 = Note(2, "Title 2", "Text 2")
        noteService.add(note1)
        noteService.add(note2)
    }

    @Test
    fun addNote() {
        val retrievedNote = noteService.getById(1)
        assertEquals(note1, retrievedNote)
    }

    @Test
    fun createComment() {
        val comment = Comment(1, 1, 123, "Test comment")
        noteService.createComment(1, comment)
        val comments = noteService.getComments(1)
        assertTrue(comments.contains(comment))
    }

    @Test
    fun delete() {
        noteService.delete(1)
        val retrievedNote = noteService.getById(1)
        assertNull(retrievedNote)
    }

    @Test
    fun deleteComment() {
        val comment = Comment(1, 1, 123, "Test comment")
        noteService.createComment(1, comment)
        noteService.deleteComment(1, 1)
        val comments = noteService.getComments(1)
        assertFalse(comments.contains(comment))
    }

    @Test
    fun edit() {
        val editedNote = Note(1, "New Title", "New Text")
        noteService.edit(editedNote)
        val retrievedNote = noteService.getById(1)
        assertEquals(editedNote, retrievedNote)
    }

    @Test
    fun editComment() {
        val comment = Comment(1, 1, 123, "Test comment")
        noteService.createComment(1, comment)
        val editedComment = Comment(1, 1, 123, "Edited comment")
        noteService.editComment(1, 1, editedComment)
        val comments = noteService.getComments(1)
        assertTrue(comments.contains(editedComment))
    }

    @Test
    fun get() {
        val allNotes = noteService.get()
        assertEquals(2, allNotes.size)
        assertEquals(note1, allNotes[0])
        assertEquals(note2, allNotes[1])
    }

    @Test
    fun getById() {
        val retrievedNote = noteService.getById(1)
        assertEquals(note1, retrievedNote)
    }

    @Test
    fun getComments() {
        val comment1 = Comment(1, 1, 123, "Comment 1")
        val comment2 = Comment(2, 1, 456, "Comment 2")
        noteService.createComment(1, comment1)
        noteService.createComment(1, comment2)
        val comments = noteService.getComments(1)
        assertEquals(2, comments.size)
        assertEquals(comment1, comments[0])
        assertEquals(comment2, comments[1])
    }

    @Test
    fun restoreCommentIsDeleted() {
        val comment = Comment(1, 1, 123, "Test comment")
        noteService.createComment(1, comment)
        val comments = noteService.getComments(1)
        assertTrue(comments.contains(comment))
    }

    @Test
    fun testCommentIsNotDeleted() {
        val comment = Comment(1, 1, 123, "Test comment")
        noteService.createComment(1, comment)
        val comments = noteService.getComments(1)
        assertFalse(comments.first { it.id == 1 }.isDeleted)
    }
}
package ru.netology

data class Post(
    val id: Int,                        // Идентификатор записи.
    val fromId: Int,                    // Идентификатор автора записи (от чьего имени опубликована запись)
    val text: String,                   // Текст записи
    val likes: Likes? = Likes(),
    val replyPostId: Int? = null,       // Идентификатор записи, в ответ на которую была оставлена текущая
    val friendsOnly: Boolean = false,   // Eсли запись была создана с опцией «Только для друзей»
    val date: Int = 0,                  // Время публикации записи
    val canPin: Boolean = true,         // может ли текущий пользователь закрепить запись
    val canDelete: Boolean = false,     // может ли текущий пользователь удалить запись
    val canEdit: Boolean = false,       // может ли текущий пользователь редактировать запись
    val isPinned: Boolean = false,      // Информация о том, что запись закреплена
    val attachment: Array<Attachment>? = null
)

data class Likes(var likes: Int = 0)

data class Comment(val id: Int, val postID: Int, val fromId: Int, var text: String) {
    var isDeleted: Boolean = false
}

open class Note(val id: Int, val title: String, val text: String)
class PostNotFoundException(message: String) : RuntimeException(message)
class NoteNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)

object WallService {

    var posts = emptyArray<Post>()
    var lastPostId = 0
    var comments = emptyArray<Comment>()

    fun createComment(postID: Int, comment: Comment): Comment {
        val post = posts.find { it.id == postID }
        if (post != null) {
            comments += comment.copy(id = comments.size +1, postID = postID)
            return comments.last()
        } else {
            throw PostNotFoundException("Пост с id $postID не найден")
        }
    }

    fun printComments() {
        for (comment in comments) {
            println(comment)
            print(" ")
            println()
        }
    }

    fun clear() {
        posts = emptyArray()
        lastPostId = 0
    }

    fun add(post: Post): Post {
        posts += post.copy(id = ++lastPostId, likes = post.likes?.copy())
        return posts.last()
    }

    fun update(newPost: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == newPost.id) {
                posts[index] = newPost.copy(likes = newPost.likes?.copy())
                return true
            }
        }
        return false
    }

    fun printPosts() {
        for (post in posts) {
            println(post)
            print(" ")
            println()
        }
    }
}

interface Attachment {
    val type: String
}

data class Photo(
    val id: Int, val owner_id: Int, val size: Int, val url: String
)

data class PhotoAttachment(val photo: Photo) : Attachment {
    override val type = "photo"
}

data class Audio(val id: Int, val owner_id: Int, val title: String, val duration: Int)
data class AudioAttachment(val audio: Audio) : Attachment {
    override val type = "audio"
}

data class Video(val id: Int, val owner_id: Int, val title: String, val duration: Int)
data class VideoAttachment(val video: Video) : Attachment {
    override val type = "video"
}

data class Document(
    val id: Int, val owner_id: Int, val title: String, val size: Int, val url_doc: String
)

data class DocumentAttachment(val document: Document) : Attachment {
    override val type = "document"
}

data class Link(
    val url: String, val title: String, val caption: String, val description: String
)

data class LinkAttachment(val link: Link) : Attachment {
    override val type = "link"
}

open class NoteService<NoteType : Note> {
    var notes = mutableListOf<NoteType>()
    var comments = mutableListOf<Comment>()

    fun add(note: NoteType): NoteType {
        notes += note
        return notes.last()
    }

    fun createComment(noteId: Int, comment: Comment): Comment {
        val note = notes.find { it.id == noteId }
        if (note != null) {
            comments += comment.copy(id = comments.size +1, postID = noteId)
            return comments.last()
        } else {
            throw NoteNotFoundException("Заметка с id $noteId не найдена")
        }
    }

    fun delete(noteId: Int) {
        val note = notes.find { it.id == noteId }
        if (note != null) {
            comments = comments.filterNot { it.postID == noteId }.toMutableList()
            notes.remove(note)
        } else {
            throw NoteNotFoundException("Заметка с id не $noteId найдена")
        }
    }

    fun deleteComment(commentId: Int, noteId: Int) {
        val comment = comments.find { it.id == commentId && it.postID == noteId }
        if (comment != null) {
            comment.isDeleted = true
        } else {
            throw CommentNotFoundException("Комментарий с id $commentId не найден")
        }
    }

    fun edit(newNote: NoteType) :Boolean {
        for ((index, note) in notes.withIndex()) {
            if (note.id == newNote.id) {
                notes[index] = newNote
                return true
            }
        }
        return false
    }

    fun editComment(commentId: Int, noteId: Int, newComment: Comment): Boolean {
        val existingComment = comments.indexOfFirst { it.id == commentId && it.postID == noteId }
        if (existingComment != -1) {
        comments[existingComment] = newComment
            return true
        }
        return false
    }
    fun get(): List<NoteType> {
        return notes
    }

    fun getById(noteId: Int): NoteType? {
        return notes.find { it.id == noteId }
    }

    fun getComments(noteId: Int): List<Comment> {
        return comments.filter { it.postID == noteId && !it.isDeleted }
    }

    fun restoreComment(commentId: Int, noteId: Int) {
        val comment = comments.find { it.id == commentId && it.postID == noteId}
        if (comment?.isDeleted == true) {
            println("Восстановление удаленного комментария невозможно")
            return
        } else {
            println("Комментарий не удален")
        }
    }
}

fun main() {
    val likes = Likes(10)
    val photoAttachment = PhotoAttachment(Photo(1, 1, 200, "photo_604_url"))
    val audioAttachment = AudioAttachment(Audio(3, 2, "My favourite song", 188))
    val videoAttachment = VideoAttachment(Video(1, 1, "A fun cat video", 30))
    val documentAttachment = DocumentAttachment(Document(2, 4, "document", 50, "url_doc"))
    val linkAttachment = LinkAttachment(Link("link_url", "The Last of Us", "Game", "Link to the game"))
    val comment =
        WallService.add(Post(1, 223894, " Hi", likes))

    WallService.add(Post(3, 672856284, " Hello"))
    WallService.printPosts()
    likes.likes = 100
    WallService.printPosts()

    println(WallService.update(Post(7, 3265, "Hi World")))
    WallService.printPosts()

    println(WallService.update(Post(2, 3265, " I am Scatman")))
    WallService.printPosts()

    WallService.add(
        Post(
            1,
            36532,
            "Hi",
            likes,
            attachment = arrayOf(photoAttachment, audioAttachment, videoAttachment, documentAttachment, linkAttachment)
        )
    )
    WallService.printPosts()

    WallService.createComment(1, Comment(1, 1, 234, "Comment on first post"))
    WallService.printComments()

    WallService.createComment(1, Comment(1, 132, 234, "Comment on non-existing post"))
    WallService.printComments()

    //создание заметок
    val noteService = NoteService<Note>()
    val note1 = Note(1, "Title 1", "Text #1")
    val note2 = Note(2, "Title 2", "Text #2")

    noteService.add(note1)
    noteService.add(note2)

    //получение списка всех заметок
    val allNotes = noteService.get()
    println("All notes")
    allNotes.forEach { println(it) }

    // Получение заметки по ее id и вывод ее на экран
    val retrievedNote1 = noteService.getById(1)
    println("Retrieved note 1:")
    println(retrievedNote1)

    //редактирование заметки
    noteService.edit(Note(1, "New title 1", "New text #1"))
    println(noteService.getById(1))
    println(noteService.get())

    //создание комментариев к заметкам
    noteService.createComment(1, Comment(1,1,435, "Comment 1 to note 1"))
    noteService.createComment(1, Comment(2,1,647, "Comment 2 to note 1"))
    noteService.createComment(2, Comment(3,2,366, "Comment 1 to note 2"))

    //получение комментариев к заметке
    var commentsToNote1 = noteService.getComments(1)
    println("Comments to note 1:")
    commentsToNote1.forEach { println(it) }

    //редактирование комментария
    noteService.editComment(1, 1, Comment(1, 1, 345, "New comment 1 to note 1"))
    println("Comments to note 1 after editing:")
    noteService.getComments(1).forEach { println(it) }

    //удаление комментария
    noteService.deleteComment(2, 1)
    println("Comments to note 1 after deletion:")
    noteService.getComments(1).forEach { println(it) }

    //удаление заметки
    noteService.delete(2)
    noteService.get()
    println("All notes")
    allNotes.forEach { println(it) }

    //попытка создания комментария к удаленной заметке
    try {
        noteService.createComment(2, Comment(4, 1, 244, "Comment 3 to note 1"))
    } catch (e:NoteNotFoundException) {
        println(e.message)
    }

    noteService.restoreComment(2, 1)
    noteService.restoreComment(1, 1)

}

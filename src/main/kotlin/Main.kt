package ru.netology

data class Post(
    val id: Int,                        // Идентификатор записи.
    val fromId: Int,                    // Идентификатор автора записи (от чьего имени опубликована запись)
    val text: String,                   // Текст записи
    val likes: Likes? = Likes(),
    val replyPostId: Int? = null,          // Идентификатор записи, в ответ на которую была оставлена текущая
    val friendsOnly: Boolean = false,   // Eсли запись была создана с опцией «Только для друзей»
    val date: Int = 0,                  // Время публикации записи
    val canPin: Boolean = true,         // может ли текущий пользователь закрепить запись
    val canDelete: Boolean = false,     // может ли текущий пользователь удалить запись
    val canEdit: Boolean = false,       // может ли текущий пользователь редактировать запись
    val isPinned: Boolean = false,      // Информация о том, что запись закреплена
    val attachment: Array<Attachment>? = null
)

data class Likes(var likes: Int = 0)

object WallService {

    var posts = emptyArray<Post>()
    var lastPostId = 0

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

fun main() {
    val likes = Likes(10)
    val photoAttachment = PhotoAttachment(Photo(1, 1, 200, "photo_604_url"))
    val audioAttachment = AudioAttachment(Audio(3, 2, "My favourite song", 188))
    val videoAttachment = VideoAttachment(Video(1, 1, "A fun cat video", 30))
    val documentAttachment = DocumentAttachment(Document(2,4,"document", 50,"url_doc"))
    val linkAttachment = LinkAttachment(Link("link_url", "The Last of Us", "Game","Link to the game"))

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
}

package ru.netology

data class Post(
    val id: Int,                        // Идентификатор записи.
    val fromId: Int,                    // Идентификатор автора записи (от чьего имени опубликована запись)
    val text: String,                   // Текст записи
    val likes: Likes = Likes(),
    val replyPostId: Int = 0,           // Идентификатор записи, в ответ на которую была оставлена текущая
    val friendsOnly: Boolean = false,   // Eсли запись была создана с опцией «Только для друзей»
    val date: Int = 0,                  // Время публикации записи
    val canPin: Boolean = true,         // может ли текущий пользователь закрепить запись
    val canDelete: Boolean = false,     // может ли текущий пользователь удалить запись
    val canEdit: Boolean = false,       // может ли текущий пользователь редактировать запись
    val isPinned: Boolean = false,      // Информация о том, что запись закреплена

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
        posts += post.copy(id = ++lastPostId, likes = post.likes.copy())
        return posts.last()

    }

    fun update(newPost: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == newPost.id) {
                posts [index] = newPost.copy(likes = newPost.likes.copy())
                return true
            }
        }
        return false
    }
    fun printPosts() {
        for (post in posts) {
            print(post)
            print(" ")
            println()
            println()
        }
    }
}


fun main() {
    val likes = Likes(10)
    WallService.add(Post(1, 223894, " Hi", likes))
    WallService.add(Post(3, 672856284, " Hello"))
    WallService.printPosts()
    likes.likes = 100
    WallService.printPosts()

    println(WallService.update(Post(7, 3265, "Hi World")))
    WallService.printPosts()

    println(WallService.update(Post(2, 3265, " I am Scatman")))
    WallService.printPosts()
}




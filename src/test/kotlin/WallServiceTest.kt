import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import ru.netology.*

class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun addAndChangeSizeToArrayList() {
        val size = WallService.posts.size
        WallService.add(Post(1, 223894, "Hi"))
        assertEquals(size + 1, WallService.posts.size)
    }

    @Test
    fun addAndChangeId() {
        val newLastPostId = WallService.lastPostId
        WallService.add(Post(1, 223894, "Hi"))
        assertEquals(newLastPostId + 1, WallService.posts.last().id)
    }

    @Test
    fun updateShouldReturnTrue() {
        WallService.add(Post(1, 3242, "Hello"))
        val updatedPost = Post(1, 3242, "Update")
        assertTrue(WallService.update(updatedPost))
    }

    @Test
    fun updateShouldReturnFalse() {
        WallService.add(Post(1, 3242, "Hello"))
        val updatedPost = Post(3, 3242, "Update")
        assertFalse(WallService.update(updatedPost))
    }

    @Test
    fun updateShouldModifyPost() {
        WallService.add(Post(1, 75880, "Scatman"))
        val updatedPost = Post(1, 75880, "I am Scatman")
        WallService.update(updatedPost)
        assertEquals(updatedPost, WallService.posts.last())
    }

    @Test
    fun createComment() {
        val newLastCommentId = WallService.comments.size
        WallService.add(Post(1, 213, "Post 1"))
        WallService.createComment(1, Comment(1, 1, 325354, "Comment"))
        assertEquals( newLastCommentId + 1, WallService.comments.size)
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrowPost() {
        WallService.add(Post(1, 213, "Post 1"))
        WallService.createComment(123, Comment(1, 123, 325354, "Comment"))
    }

    @Test(expected = NoteNotFoundException::class)
    fun shouldThrowCreateCommentToNote() {
    createComment()

    }
}


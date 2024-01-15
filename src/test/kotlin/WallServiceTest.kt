import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import ru.netology.Post
import ru.netology.WallService

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
}
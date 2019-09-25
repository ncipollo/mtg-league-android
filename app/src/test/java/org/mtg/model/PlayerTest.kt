package org.mtg.model

import org.junit.Test
import kotlin.test.assertEquals

class PlayerTest {

    @Test
    fun plus() {
        val player = Player() + 1
        assertEquals(expected = Player(score = Score() + 1), actual = player)
    }

    @Test
    fun minus() {
        val player = Player() - 1
        assertEquals(expected = Player(score = Score() - 1), actual = player)
    }
}

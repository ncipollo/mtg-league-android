package org.mtg.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.squareup.sqldelight.Query
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.data.Settings
import org.mtg.data.SettingsQueries
import org.mtg.rx.TestSchedulers

@RunWith(JUnit4::class)
class SettingsLocalRepositoryTest {
    private val settings = mock<Settings>() {
        on { id } doReturn SettingsLocalRepository.ID
        on { darkMode } doReturn true
        on { selectedLeagueId } doReturn 123
        on { selectedLeagueName } doReturn "M20"
    }

    private val query = mock<Query<Settings>> {
        on { executeAsList() } doReturn listOf(settings)
    }
    private val queries = mock<SettingsQueries> {
        on { get(SettingsLocalRepository.ID) } doReturn query
    }
    private val schedulers = TestSchedulers()
    private val repo = SettingsLocalRepository(queries, schedulers)


    @Test
    fun get() {
        val observer = repo.get().test()
        observer.assertValue(listOf(settings))
    }

    @Test
    fun update() {
        val observer = repo.update(settings).test()

        observer.assertComplete()
        verify(queries).delete(SettingsLocalRepository.ID)
        verify(queries).insert(
            id = settings.id,
            darkMode = settings.darkMode,
            selectedLeagueId = settings.selectedLeagueId,
            selectedLeagueName = settings.selectedLeagueName
        )
    }
}

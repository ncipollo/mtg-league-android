package org.mtg.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.sqldelight.Query
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.entity.SettingsEntity
import org.mtg.entity.SettingsQueries
import org.mtg.model.Settings
import org.mtg.rx.TestSchedulers

@RunWith(JUnit4::class)
class SettingsLocalRepositoryTest {
    private companion object {
        val SETTINGS = Settings(
            darkMode = true,
            selectedLeagueId = 123,
            selectedLeagueName = "M20"
        )
    }

    private val entity = mock<SettingsEntity>() {
        on { id } doReturn SettingsLocalRepository.ID
        on { darkMode } doReturn SETTINGS.darkMode
        on { selectedLeagueId } doReturn SETTINGS.selectedLeagueId
        on { selectedLeagueName } doReturn SETTINGS.selectedLeagueName
    }

    private val query = mock<Query<SettingsEntity>>()
    private val queries = mock<SettingsQueries> {
        on { get(SettingsLocalRepository.ID) } doReturn query
    }
    private val schedulers = TestSchedulers()
    private val repo = SettingsLocalRepository(queries, schedulers)


    @Test
    fun get_empty() {
        prepareEmptyQuery()
        val observer = repo.get().test()

        observer.assertValue(Settings())
    }

    @Test
    fun get_settingsEntity() {
        prepareSettingsQuery()
        val observer = repo.get().test()

        observer.assertValue(SETTINGS)
    }

    @Test
    fun update() {
        val observer = repo.update(SETTINGS).test()

        observer.assertComplete()
        verify(queries).delete(SettingsLocalRepository.ID)
        verify(queries).insert(
            id = SettingsLocalRepository.ID,
            darkMode = SETTINGS.darkMode,
            selectedLeagueId = SETTINGS.selectedLeagueId,
            selectedLeagueName = SETTINGS.selectedLeagueName
        )
    }

    private fun prepareEmptyQuery() {
        whenever(query.executeAsOneOrNull()) doReturn null
        whenever(query.executeAsList()) doReturn emptyList()
    }

    private fun prepareSettingsQuery() {
        whenever(query.executeAsOneOrNull()) doReturn entity
        whenever(query.executeAsList()) doReturn listOf(entity)
    }
}

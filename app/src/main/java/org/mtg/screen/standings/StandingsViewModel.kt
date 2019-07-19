package org.mtg.screen.standings

import org.mtg.arch.ItemViewState
import org.mtg.model.Standing
import org.mtg.repository.LeagueRemoteRepository
import org.mtg.repository.StandingRemoteRepository
import org.mtg.viewmodel.MagicViewModel

class StandingsViewModel(
    private val standingRepository: StandingRemoteRepository,
    private val leagueRepository: LeagueRemoteRepository
) : MagicViewModel() {
    sealed class StandingsViewState {
        object InProgress : StandingsViewState()
        data class Success(val items: List<ItemViewState>) : StandingsViewState()
    }

    fun standings() =
        leagueRepository.leagues()
            .flatMap { leagues ->
                standingRepository
                    .standingsForLeague(leagues.last().id)
                    .map<StandingsViewState> { standings ->
                        StandingsViewState.Success(standings.map { createStandingItem(it) })
                    }
            }
            .toObservable()
            .startWith(StandingsViewState.InProgress)
            .toLiveData()


    private fun createStandingItem(standing: Standing) =
        StandingViewItem(
            overallRecord = standing.overallRecord,
            rank = standing.ranking,
            name = "${standing.firstName} ${standing.lastName}",
            location = standing.office,
            weekOneRecord = standing.week1Record,
            weekTwoRecord = standing.week2Record,
            weekThreeRecord = standing.week3Record,
            weekFourRecord = standing.week4Record
        )
}

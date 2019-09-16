package org.mtg.screen.standings

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx2.asObservable
import org.mtg.arch.ItemViewState
import org.mtg.domain.CurrentLeagueUseCase
import org.mtg.model.Standing
import org.mtg.repository.StandingRemoteRepository
import org.mtg.viewmodel.MagicViewModel

class StandingsViewModel(
    private val currentLeagueUseCase: CurrentLeagueUseCase,
    private val standingRepository: StandingRemoteRepository
) : MagicViewModel() {
    sealed class StandingsViewState {
        object InProgress : StandingsViewState()
        data class Success(val items: List<ItemViewState>) : StandingsViewState()
    }

    fun standings() =
        currentLeagueUseCase.get()
            .switchMap<StandingsViewState> { leagueResult ->
                standingRepository
                    .standingsForLeague(leagueResult.leagueId)
                    .map { standings ->
                        StandingsViewState.Success(standings.map { createStandingItem(it) })
                    }
                    .asObservable()

            }
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

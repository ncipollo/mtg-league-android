package org.mtg.screen.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.mtg.arch.ItemViewState
import org.mtg.model.Standing
import org.mtg.repository.LeagueRemoteRepository
import org.mtg.repository.StandingRemoteRepository

class StandingsViewModel(
    private val standingRepository: StandingRemoteRepository,
    private val leagueRepository: LeagueRemoteRepository
) : ViewModel() {
    private val standingsLiveData = MutableLiveData<StandingsViewState>()
    private val disposeBag = CompositeDisposable()

    sealed class StandingsViewState {
        object InProgress : StandingsViewState()
        data class Success(val items: List<ItemViewState>) : StandingsViewState()
    }

    fun standings(): LiveData<StandingsViewState> {
        disposeBag.add(
            leagueRepository.leagues().flatMap { leagues ->
                standingRepository.standingsForLeague(leagues.last().id).map { standings ->
                    StandingsViewState.Success(standings.map { createStandingItem(it) })
                }
            }.subscribe { state -> standingsLiveData.postValue(state) })

        return standingsLiveData
    }

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

    override fun onCleared() {
        disposeBag.dispose()
    }
}

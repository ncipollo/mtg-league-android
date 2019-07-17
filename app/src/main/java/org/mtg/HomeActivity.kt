package org.mtg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject
import org.mtg.api.*

class HomeActivity : AppCompatActivity() {
    private val leagueApi by inject<LeagueApi>()
    private val matchesApi by inject<MatchResultApi>()
    private val standingApi by inject<StandingApi>()
    private val userApi by inject<UserApi>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        testLeague()
        testMatches()
        testStanding()
        testUser()
    }

    private fun testLeague() {
        leagueApi.leagues()
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    private fun testMatches() {
        matchesApi.matchResults()
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }

        leagueApi.matchResultsForLeague(8)
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }

        userApi.matchResultsForUser(1)
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    private fun testStanding() {
        standingApi.standingForLeague(8)
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    private fun testUser() {
        userApi.users()
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}

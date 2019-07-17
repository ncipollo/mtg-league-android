package org.mtg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject
import org.mtg.api.LeagueApi
import org.mtg.api.UserApi
import org.mtg.api.onApiErrorReturn

class HomeActivity : AppCompatActivity() {
    private val leagueApi by inject<LeagueApi>()
    private val userApi by inject<UserApi>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        testLeague()
        testUser()
    }

    private fun testLeague() {
        leagueApi.leagues()
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

package org.mtg.repository

import org.mtg.api.UserApi
import org.mtg.api.onApiErrorReturn
import timber.log.Timber

class UserRemoteRepository(private val userApi: UserApi) {
    fun users() =
        userApi.users()
            .doOnError { Timber.w("users error: $it") }
            .onApiErrorReturn { emptyList() }
}

package org.mtg.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.api.UserApi
import org.mtg.model.User
import java.io.IOException

@RunWith(JUnit4::class)
class UserRemoteRepositoryTest {

    private companion object {
        val ERROR = IOException()
        val USER = User(
            id = 1,
            firstName = "Nick",
            lastName = "Cipollo",
            email = "nope@email.com"
        )
    }


    private val userSubject = PublishSubject.create<List<User>>()
    private val api = mock<UserApi> {
        on { users() } doReturn userSubject.firstOrError()
    }
    private val repo = UserRemoteRepository(api)

    @Test
    fun users_error() {
        val observer = repo.users().test()
        respondWithUserError()
        observer.assertValue(emptyList())
    }

    @Test
    fun users_success() {
        val observer = repo.users().test()
        respondWithUsers()
        observer.assertValue(listOf(USER))
    }

    private fun respondWithUsers() = userSubject.onNext(listOf(USER))

    private fun respondWithUserError() = userSubject.onError(ERROR)
}

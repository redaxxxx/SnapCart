package com.android.developer.prof.reda.snapcart.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.developer.prof.reda.snapcart.MainCoroutineRule
import com.android.developer.prof.reda.snapcart.util.Resource
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class LoginViewModelTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_auth")
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup(){
        //inject?
        hiltRule.inject()
        viewModel = LoginViewModel(firebaseAuth)
    }

    @Test
     fun loginTest()= runTest{
        viewModel.loginUser("redanabil518@gmail.com", "01125572678")

        viewModel.login.collectLatest {
            assertThat(it).isEqualTo(Resource.Success(it.data))
        }

    }

}
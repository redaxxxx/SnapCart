package com.android.developer.prof.reda.snapcart.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_auth")
    fun provideFirebaseAuthTest() = FirebaseAuth.getInstance()
    @Provides
    @Named("test_db")
    fun provideFirestoreTest() = FirebaseFirestore.getInstance()
}
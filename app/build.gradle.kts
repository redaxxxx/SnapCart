plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
    id ("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.android.developer.prof.reda.snapcart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.developer.prof.reda.snapcart"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.android.developer.prof.reda.snapcart.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-firestore:25.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    testImplementation("com.google.truth:truth:1.0.1")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Android Ktx
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.2")

    //Dagger hilt
    implementation ("com.google.dagger:hilt-android:2.50")
    kapt ("com.google.dagger:hilt-compiler:2.50")


    //Firebase
    implementation ("com.google.firebase:firebase-auth:21.0.6")
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database:21.0.0")

    //Coroutines with firebase
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.1")

    //loading button
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    //Navigation component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.2")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.2")


    // AndroidX Test - JVM testing
    testImplementation ("androidx.test.ext:junit-ktx:1.2.1")

    testImplementation ("androidx.test:core-ktx:1.6.1")

    testImplementation ("org.robolectric:robolectric:4.13")

    // Other dependencies
    testImplementation ("org.hamcrest:hamcrest-all:1.1")

    // Instrumented unit tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.28-alpha")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.28-alpha")

    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    testImplementation ("androidx.arch.core:core-testing:2.2.0")

    // For Robolectric tests.
    testImplementation("com.google.dagger:hilt-android-testing:2.44")
    // ...with Kotlin.
    kaptTest("com.google.dagger:hilt-android-compiler:2.44")
    // ...with Java.
    testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.44")

}
kapt{
    correctErrorTypes = true
}

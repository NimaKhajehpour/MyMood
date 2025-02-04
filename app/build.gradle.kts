//import com.android.tools.profgen.ArtProfileKt
//import com.android.tools.profgen.ArtProfileSerializer
//import com.android.tools.profgen.DexFile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

//project.afterEvaluate {
//    tasks.named("compileReleaseArtProfile").configure {
//        doLast {
//            outputs.files.forEach { file ->
//                if (file.toString().endsWith(".profm")) {
//                    println("Sorting $file ...")
//                    val version = ArtProfileSerializer.valueOf("METADATA_0_0_2")
//                    val profile = ArtProfileKt.ArtProfile(file)
//                    val keys = ArrayList(profile.profileData.keys)
//                    val sortedData = linkedMapOf<Any, Any>()
//                    Collections.sort(keys, DexFile.Companion())
//                    keys.forEach { key -> sortedData[key] = profile.profileData[key] }
//                    FileOutputStream(file).use {
//                        it.write(version.magicBytes$profgen)
//                        it.write(version.versionBytes$profgen)
//                        version.write$profgen(it, sortedData, "")
//                    }
//                }
//            }
//        }
//    }
//}

android {
    namespace = "com.nima.mymood"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nima.mymood"
        minSdk = 21
        targetSdk = 33
        versionCode = 10
        versionName = "1.5.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {

    //fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Datastore
    implementation(libs.datastore.preferences)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Lifecycle
    implementation(libs.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    debugImplementation(libs.ui.tooling)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)

    // Navigation
    implementation(libs.navigation.compose)

    // Room Database
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // Koin
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.navigation)

    // Activity
    implementation(libs.activity.compose)

    // Core
    implementation(libs.core.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)


}
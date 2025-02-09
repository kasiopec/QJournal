import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.kasiopec.qjournal"

    compileSdk = 35

    defaultConfig {
        applicationId = "com.kasiopec.qjournal"
        minSdk = 25
        targetSdk = 35
        versionCode = 5
        versionName = "2.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val keystoreProperties = Properties()
            val keystoreFile = rootProject.file("local.properties")
            if (keystoreFile.exists()) {
                keystoreProperties.load(keystoreFile.inputStream())
            }
            storeFile = file(keystoreProperties.getProperty("QJOURNAL_KEYSTORE_PATH"))
            storePassword = keystoreProperties.getProperty("QJOURNAL_STORE_PASSWORD")
            keyPassword = keystoreProperties.getProperty("QJOURNAL_KEY_PASSWORD")
            keyAlias = keystoreProperties.getProperty("QJOURNAL_KEY_ALIAS")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Use optimized version
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraint.layout)
    implementation(libs.legacy.support)
    implementation(libs.recycler.view)
    implementation(libs.drawer.layout)

    implementation(libs.android.charts)

    implementation(libs.firebase.analytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso)
}

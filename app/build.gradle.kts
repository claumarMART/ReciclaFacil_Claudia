plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.3.4"  // plugin ksp (agregar primero y sincronizar)
}

android {
    namespace = "com.juandeherrera.reciclafacil"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.juandeherrera.reciclafacil"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.compose.material3:material3:1.4.0") // dependencia de actualización de Material3
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.5.0-alpha11") // navegacion adaptativa segun el tamaño de pantalla
    implementation("androidx.compose.material3:material3-window-size-class:1.4.0") // herramientas para clasificar el tamaño de la pantalla

    // dependencias para el uso de iconos
    implementation("androidx.compose.material:material-icons-extended:1.5.0-alpha11") // dependencia para iconos de formularios
    implementation("com.composables:icons-lucide:1.1.0") // dependencia para la libreria de iconos Lucide

    // dependencias para usar la base de datos local
    implementation("androidx.room:room-runtime:2.8.4") // dependencia principal para poder usar Room
    ksp("androidx.room:room-compiler:2.8.4")           // dependencia que permite usar el procesador de anotaciones de Room (KSP)
    implementation("androidx.room:room-ktx:2.8.4")     // dependencia que agrega extensiones de Kotlin para Room
    implementation("io.coil-kt:coil-compose:2.7.0")    // dependencia para usar imagenes desde Internet (AsyncImage)

    // dependencias para notificaciones
    implementation(platform("androidx.compose:compose-bom:2025.12.00")) // dependencia para notificaciones en Snackbar
    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha") // dependencia para notficaciones con icono

    implementation(libs.androidx.navigation.compose) // dependencia para usar la navegación entre pantallas

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
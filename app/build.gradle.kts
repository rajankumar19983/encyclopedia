plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.compose")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.rajankumar.encyclopaedia"
  compileSdk = 36

  defaultConfig {
    applicationId = "com.rajankumar.encyclopaedia"
    minSdk = 26
    targetSdk = 36
    versionCode = 1
    versionName = "0.1.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.17.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
  implementation("androidx.activity:activity-compose:1.12.1")
  implementation("androidx.navigation:navigation-compose:2.9.5")
  implementation("androidx.room:room-runtime:2.8.4")
  implementation("androidx.room:room-ktx:2.8.4")
  ksp("androidx.room:room-compiler:2.8.4")

  // Bundled Latin-script OCR model: available immediately and works offline.
  implementation("com.google.mlkit:text-recognition:16.0.1")

  implementation(platform("androidx.compose:compose-bom:2026.02.01"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.compose.material:material-icons-extended")

  testImplementation("junit:junit:4.13.2")
  debugImplementation("androidx.compose.ui:ui-tooling")
}

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("maven-publish")
}

android {
    namespace = "com.ent21.expandablerowchipgroup"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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

    publishing {
        multipleVariants {
            allVariants()
            withJavadocJar()
            withSourcesJar()
        }
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenRelease") {
                from(components.findByName("release"))
                groupId = "com.ent21"
                artifactId = "expandable-row-chip-group"
                version = "1.0"
            }
            create<MavenPublication>("mavenDebug") {
                from(components.findByName("debug"))
                groupId = "com.ent21"
                artifactId = "expandable-row-chip-group"
                version = "1.0"
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.plugin)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.vanniktech.publish)
}

buildConfig {
    packageName("io.zyxn.compiler.plugin")

    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"io.github.zyxn-dev.compiler.plugin\"")

    val pluginProject = project(":zyxn-compiler-plugin")
    buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${pluginProject.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${pluginProject.name}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${pluginProject.version}\"")

    val apiProject = project(":zyxn-api")
    buildConfigField(
        type = "String",
        name = "ZYXN_API_LIBRARY_COORDINATES",
        expression = "\"${apiProject.group}:${apiProject.name}:${apiProject.version}\""
    )

    buildConfigField("String", "KOTLIN_VERSION", "\"${libs.versions.kotlin}\"")
    buildConfigField("String", "AGP_VERSION", "\"${libs.versions.agp}\"")
}

gradlePlugin {
    plugins {
        create("ZyxnCompilerGradleSubplugin") {
            id = "io.github.zyxn-dev.compiler.plugin"
            displayName = "ZyxnPlugin"
            description = "ZyxnCompilerGradleSubplugin"
            implementationClass = "io.zyxn.compiler.plugin.ZyxnCompilerGradleSubplugin"
        }
    }
}

dependencies {
    compileOnly(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation(libs.compose.gradle.plugin)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.android.tools)
}

plugins {
    antlr
    application
    java
    `maven-publish`
    id("com.github.ben-manes.versions") version "0.44.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.cloud.tools.jib") version "3.3.1"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    val neo4jVersion = "4.0.3"
    antlr("com.tunnelvisionlabs:antlr4:4.9.0")
    implementation("com.j2html:j2html:1.6.0")
    implementation("info.picocli:picocli:4.7.0")
    implementation("io.smallrye.config:smallrye-config:3.1.1")
    implementation("io.smallrye.config:smallrye-config-source-yaml:3.1.1")
    implementation("org.apache.logging.log4j:log4j-api:2.19.0")
    implementation("org.apache.logging.log4j:log4j-core:2.19.0")
    implementation("org.eclipse.jdt:org.eclipse.jdt.core:3.32.0")
    implementation("org.json:json:20220924")
    implementation("org.neo4j.driver:neo4j-java-driver:$neo4jVersion")
    implementation("org.yaml:snakeyaml:1.33")
    runtimeOnly("com.tunnelvisionlabs:antlr4-runtime:4.9.0")
    testCompileOnly("org.neo4j:neo4j:$neo4jVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.neo4j.procedure:apoc:4.0.0.15")
    testImplementation("org.neo4j.test:neo4j-harness:$neo4jVersion")
}

sourceSets {
    main {
        antlr {
            setSrcDirs(files("src/main/resources"))
        }
    }
}

application {
    mainClass.set("MainCli")
}

tasks.generateGrammarSource {
    arguments.addAll(listOf("-package", "org.antlr.v4.generatedsources.cpp14"))
    arguments.addAll(listOf("-visitor", "-no-listener"))
}

tasks.withType<AbstractArchiveTask> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

group = "fr.unice.i3s.sparks.al"
version = "1.0-SNAPSHOT"
description = "symfinder"

tasks.withType<JavaCompile> {
    options.release.set(11)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.test {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(11))
    })
    useJUnitPlatform()
    testLogging {
        setEvents(listOf("passed", "skipped", "failed"))
    }
}

jib {
    from {
        image = "docker://symfinder-engine-base:17-jre-alpine"
    }
    extraDirectories {
        paths {
            path {
                setFrom("docker/symfinder/")
                into = "/"
            }
        }
    }
    container {
        entrypoint = mutableListOf("sh", "exec_symfinder.sh")
        environment = mapOf("SYMFINDER_VERSION" to version.toString())
    }
    to {
        image = "symfinder-engine"
        tags = setOf(version.toString())
    }
}

tasks.jibDockerBuild {
    doFirst {
        jib?.to?.tags = setOf("local")
    }
}
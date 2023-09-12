dependencies {
    implementation ("javax.json:javax.json-api:1.1.4")
    implementation ("com.google.guava:guava")
    implementation ("com.fasterxml.jackson.core:jackson-databind")
    implementation ("javax.json:javax.json-api")
    implementation ("org.glassfish:jakarta.json")
    implementation ("com.google.protobuf:protobuf-java-util")
    compileOnly ("org.projectlombok:lombok:1.18.28")
    annotationProcessor ("org.projectlombok:lombok:1.18.28")

    testCompileOnly ("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.28")

    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testImplementation ("org.assertj:assertj-core")
    testImplementation ("org.mockito:mockito-core")
    testImplementation ("org.mockito:mockito-junit-jupiter")
}


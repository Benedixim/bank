plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("mysql:mysql-connector-java:8.0.25")
    compileOnly ("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/com.itextpdf/itextpdf
    implementation ("com.itextpdf:itextpdf:5.5.13.3")
    // https://mvnrepository.com/artifact/org.yaml/snakeyaml
    implementation ("org.yaml:snakeyaml:2.2")


}

tasks.test {
    useJUnitPlatform()

}
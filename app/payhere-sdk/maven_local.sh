mkdir -p androidsdk/build/publications/Production/
cp pom-default.xml androidsdk/build/publications/Production/
./gradlew --no-configure-on-demand --no-parallel :clean :androidsdk:assembleRelease :androidsdk:publishReleasePublicationToMavenLocal
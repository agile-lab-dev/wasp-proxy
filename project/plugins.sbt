resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)



addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0")
/*
 * Bintray publishing plugin
 */
addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
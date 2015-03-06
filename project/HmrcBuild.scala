/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt._
import sbt.Keys._

object HmrcBuild extends Build {

  import uk.gov.hmrc._
  import DefaultBuildSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}
  import PublishingSettings._
  import NexusPublishing._
  import scala.util.Properties.envOrElse

  val appName = "http-verbs"
  val appVersion = envOrElse("HTTP_VERBS_VERSION", "999-SNAPSHOT")

  lazy val microservice = Project(appName, file("."))
    .settings(version := appVersion)
    .settings(scalaSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      targetJvm := "jvm-1.7",
      shellPrompt := ShellPrompt(appVersion),
      libraryDependencies ++= AppDependencies(),
      Collaborators(),
      crossScalaVersions := Seq("2.11.5")
    )
    .settings(publishAllArtefacts : _*)
    .settings(nexusPublishingSettings : _*)
    .settings(SbtBuildInfo(): _*)
}

private object AppDependencies {

  import play.PlayImport._
  import play.core.PlayVersion

  val compile = Seq(
    "com.typesafe.play" %% "play" % PlayVersion.current,
    ws % "provided",
    "net.ceedubs" %% "ficus" % "1.1.1",
    "uk.gov.hmrc" %% "time" % "1.1.0",
    "uk.gov.hmrc" %% "http-exceptions" % "0.3.0"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test: Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "commons-codec" % "commons-codec" % "1.7" % scope,
        "org.scalatest" %% "scalatest" % "2.2.4" % scope,
        "org.pegdown" % "pegdown" % "1.4.2" % scope,
        "com.github.tomakehurst" % "wiremock" % "1.52" % scope
      )
    }.test
  }

  def apply() = compile ++ Test()
}


object Collaborators {

  def apply() = {
    pomExtra := (<url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git@github.tools.tax.service.gov.uk:HMRC/http-verbs.git</connection>
        <developerConnection>scm:git@github.tools.tax.service.gov.uk:HMRC/http-verbs.git</developerConnection>
        <url>git@github.tools.tax.service.gov.uk:HMRC/http-verbs.git</url>
      </scm>)
  }
}

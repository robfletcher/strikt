import com.fasterxml.jackson.databind.ObjectMapper
import com.ferranpons.twitterplugin.TwitterPluginExtension
import com.ferranpons.twitterplugin.TwitterUpdate
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import java.io.IOException
import java.net.URL

plugins {
  id("com.ferranpons.twitterplugin")
}

val tag = System.getenv("CIRCLE_TAG")
if (tag != null) {
  val ghToken = System.getenv("GITHUB_OAUTH_TOKEN")
  val ghReleaseUrl =
    URL("https://api.github.com/repos/robfletcher/strikt/releases/tags/$tag?access_token=$ghToken")

  try {
    val releaseName = ghReleaseUrl.readText().let {
      ObjectMapper().readTree(it)["name"]
    }

    configure<TwitterPluginExtension> {
      consumerKey = System.getenv("TWITTER_CONSUMER_KEY")
      consumerSecret = System.getenv("TWITTER_CONSUMER_SECRET")
      accessToken = System.getenv("TWITTER_ACCESS_TOKEN")
      accessTokenSecret = System.getenv("TWITTER_ACCESS_TOKEN_SECRET")
      message = "Strikt $tag $releaseName is available. https://strikt.io"
    }

    val createTweet by tasks.getting(TwitterUpdate::class)
    afterEvaluate {
      tasks.getByName("postRelease").finalizedBy(createTweet)
    }
  } catch (e: IOException) {
    logger.warn("Could not get release name from GitHub: ${e.message}")
  }
}

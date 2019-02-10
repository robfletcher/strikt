import com.fasterxml.jackson.databind.ObjectMapper
import com.ferranpons.twitterplugin.TwitterPluginExtension
import com.ferranpons.twitterplugin.TwitterUpdate
import java.io.IOException
import java.net.URL

plugins {
  id("com.ferranpons.twitterplugin")
}

System.getenv("CIRCLE_TAG")?.let { tag ->
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
      message = "Strikt $tag $releaseName is available. https://strikt.io\n\nRelease notes: https://github.com/robfletcher/strikt/releases/$tag"
    }

    val createTweet by tasks.getting(TwitterUpdate::class)
    afterEvaluate {
      tasks.getByName("postRelease").finalizedBy(createTweet)
    }
  } catch (e: IOException) {
    logger.warn("Could not get release name from GitHub: ${e.message}")
  }
}

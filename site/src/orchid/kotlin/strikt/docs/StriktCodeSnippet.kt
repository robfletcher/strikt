package strikt.docs

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import java.util.regex.Pattern
import javax.inject.Inject


class StriktCodeSnippetTag
@Inject
constructor(
  val context: OrchidContext
) : TemplateTag("example", Type.Tabbed, true) {

  @Option
  @Description("The name of a test method in a strikt-core test class.")
  lateinit var snippets: List<String>

  @Option
  @Description("The title for the snippet")
  lateinit var title: String

  @Option
  @StringDefault("strikt/docs/DocsSnippets")
  @Description("The path to a test class in the strikt-core module, relative to the test '/kotlin' root.")
  lateinit var testClass: String

  override fun parameters(): Array<String> {
    return arrayOf("title")
  }

  override fun getNewTab(key: String?, content: String?): TemplateTag.Tab {
    return Snippet(key, content)
  }

  inner class Snippet(private val key: String?, private val content: String?) : TemplateTag.Tab {

    @Option
    @Description("The title of the tab")
    lateinit var title: String

    override fun getKey(): String? {
      return key
    }

    override fun getContent(): String? {
      return content
    }

    override fun parameters(): Array<String> {
      return arrayOf("title")
    }

    fun getSnippetContent(): String {
      val resource = context.getLocalResourceEntry("../../../../strikt-core/src/test/kotlin/$testClass.kt")
      val content = resource?.rawContent ?: ""

      val pattern = Pattern.compile("(?s)// START $key(.*?)// END $key")

      val m = pattern.matcher(content)

      return if (m.find()) m.group(1).trimIndent().trim() else throw IllegalArgumentException("Snippet $key not found in $testClass")
    }

  }


}

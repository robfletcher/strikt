package strikt.docs

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.models.Social
import com.eden.orchid.utilities.addToSet
import javax.inject.Inject

class StriktModule : OrchidModule() {

  override fun configure() {
    addToSet<Theme, StriktTheme>()
    addToSet<TemplateTag>(
      StriktCodeSnippetsTag::class,
      StriktCodeSnippetTag::class
    )
  }
}

class StriktTheme
@Inject
constructor(context: OrchidContext) : Theme(context, "StriktTheme", 10) {

  @Option
  lateinit var social: Social

  override fun loadAssets() {
    addCss("assets/css/bulma.min.css")
    addJs("assets/js/init.js")
  }
}

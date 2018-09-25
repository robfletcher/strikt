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
    addToSet<TemplateTag, StriktCodeSnippetTag>()
  }

}


class StriktTheme
@Inject
constructor(context: OrchidContext) : Theme(context, "Strikt", 10) {

  @Option
  lateinit var social: Social

  override fun loadAssets() {
    addCss("assets/css/bulma.min.css")
    addCss("assets/css/prism.css")
    addCss("assets/css/kotlindoc.scss")
    addCss("assets/css/orchidSearch.scss")

    addJs("assets/js/init.js")
    addJs("assets/js/prism.js")

    addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js")
    addJs("https://unpkg.com/lunr/lunr.js")
    addJs("assets/js/orchidSearch.js")
  }

}

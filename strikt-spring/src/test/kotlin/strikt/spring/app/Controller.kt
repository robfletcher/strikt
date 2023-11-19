package strikt.spring.app

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.APPLICATION_XML_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/"])
class Controller {
  @GetMapping(
    produces = [APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE]
  )
  fun list(): List<Album> =
    listOf(
      Album("Almost Killed Me", 2004),
      Album("Separation Sunday", 2004),
      Album("Boys and Girls in America", 2006),
      Album("Stay Positive", 2008),
      Album("Heaven Is Whenever", 2010),
      Album("Teeth Dreams", 2014)
    )
}

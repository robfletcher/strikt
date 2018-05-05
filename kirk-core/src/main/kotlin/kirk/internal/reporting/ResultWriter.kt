package kirk.internal.reporting

import kirk.api.Result

internal interface ResultWriter {
  fun write(result: Result)
  fun write(results: Iterable<Result>) = results.forEach(::write)
}
package strikt.internal.peek

class ParsedAssertInstruction(condition: String) {
  val subject: String
  val methodName: String
  val methodParameter: String

  init {
    val subjectStart = condition.indexOf("that(") + 5
    val subjectEnd = findMatchingClosingBracket(condition, subjectStart)
    subject = condition.substring(subjectStart, subjectEnd).trim()

    val isMethodCall = condition[subjectEnd + 1] == '.'
    if (isMethodCall) {
      val methodNameStart = subjectEnd + 2
      val methodNameEnd = condition.indexOf('(', methodNameStart)
      methodName = condition.substring(methodNameStart, methodNameEnd).trim()
      val parameterStart = methodNameEnd + 1
      val parameterEnd = findMatchingClosingBracket(condition, parameterStart)
      methodParameter = condition.substring(parameterStart, parameterEnd)
    } else {
      try {
        methodName = condition.substring(subjectEnd + 1).trim()
        methodParameter = ""
      } catch (e: Exception) {
        throw RuntimeException("could not parse $condition")
      }
    }
  }

  private fun findMatchingClosingBracket(condition: String, start: Int): Int {
    val len = condition.length
    var bracketLevel = 0
    var pos = start
    while (pos < len) {
      when (condition[pos]) {
        '(' -> bracketLevel += 1
        ')' -> if (bracketLevel == 0) return pos else bracketLevel -= 1
      }
      pos += 1
    }
    throw RuntimeException("could not find matching brackets in $condition")
  }
}

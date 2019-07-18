package strikt.internal.peek

internal class ParsedMethodCall(line: String, methodName: String) {
  val body: String

  init {
    val firstPossibleBracket = line.indexOf(methodName) + methodName.length
    val firstBracket = line.indexOf('{', firstPossibleBracket) + 1
    val subjectEnd = findMatchingClosingBracket(line, firstBracket)
    body = line.substring(firstBracket, subjectEnd).trim()
  }

  private fun findMatchingClosingBracket(condition: String, start: Int): Int {
    val len = condition.length
    var bracketLevel = 0
    var pos = start
    while (pos < len) {
      when (condition[pos]) {
        '{' -> bracketLevel += 1
        '}' -> if (bracketLevel == 0) return pos else bracketLevel -= 1
      }
      pos += 1
    }
    error("could not find matching brackets in $condition")
  }
}

package strikt.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.node.JsonNodeType.ARRAY
import com.fasterxml.jackson.databind.node.JsonNodeType.BOOLEAN
import com.fasterxml.jackson.databind.node.JsonNodeType.NUMBER
import com.fasterxml.jackson.databind.node.JsonNodeType.OBJECT
import com.fasterxml.jackson.databind.node.JsonNodeType.STRING
import com.fasterxml.jackson.databind.node.NumericNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import strikt.api.Assertion

/**
 * Asserts that the subject node has a field named [fieldName].
 */
fun <T : JsonNode> Assertion.Builder<T>.has(fieldName: String): Assertion.Builder<T> =
  assert("has a field named '$fieldName'") { subject ->
    if (subject.has(fieldName)) {
      pass()
    } else {
      fail(subject.fields().asSequence().map { it.key }.toList())
    }
  }

/**
 * Maps the subject node of the assertion builder to the node accessed by
 * [fieldName].
 *
 * @return an assertion builder whose subject is the named field.
 */
fun <T : JsonNode> Assertion.Builder<T>.path(fieldName: String): Assertion.Builder<JsonNode> =
  get { path(fieldName) }

/**
 * Asserts that the subject node is a JSON object.
 *
 * @return an assertion builder over the same subject that is now known to be
 * an [ObjectNode].
 */
fun <T : JsonNode> Assertion.Builder<T>.isObject(): Assertion.Builder<ObjectNode> =
  assertNodeTypeIs(OBJECT)

/**
 * Asserts that the subject node is a JSON array.
 *
 * @return an assertion builder over the same subject that is now known to be
 * an [ArrayNode].
 */
fun <T : JsonNode> Assertion.Builder<T>.isArray(): Assertion.Builder<ArrayNode> =
  assertNodeTypeIs(ARRAY)

/**
 * Asserts that the subject node is a JSON text node.
 *
 * @return an assertion builder over the same subject that is now known to be
 * a [TextNode].
 */
fun <T : JsonNode> Assertion.Builder<T>.isTextual(): Assertion.Builder<TextNode> =
  assertNodeTypeIs(STRING)

/**
 * Asserts that the subject node is a JSON numeric node.
 *
 * @return an assertion builder over the same subject that is now known to be
 * a [NumericNode].
 */
fun <T : JsonNode> Assertion.Builder<T>.isNumber(): Assertion.Builder<NumericNode> =
  assertNodeTypeIs(NUMBER)

/**
 * Asserts that the subject node is a JSON boolean node.
 *
 * @return an assertion builder over the same subject that is now known to be
 * a [BooleanNode].
 */
fun <T : JsonNode> Assertion.Builder<T>.isBoolean(): Assertion.Builder<BooleanNode> =
  assertNodeTypeIs(BOOLEAN)

@Suppress("UNCHECKED_CAST")
private fun <T : JsonNode, R : JsonNode> Assertion.Builder<T>.assertNodeTypeIs(
  type: JsonNodeType
): Assertion.Builder<R> =
  assert("is a $type node") {
    when (it.nodeType) {
      type -> pass()
      else -> fail(actual = it.nodeType, description = "found a %s node")
    }
  } as Assertion.Builder<R>

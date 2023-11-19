package strikt.jackson

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.node.JsonNodeType.ARRAY
import com.fasterxml.jackson.databind.node.JsonNodeType.BOOLEAN
import com.fasterxml.jackson.databind.node.JsonNodeType.MISSING
import com.fasterxml.jackson.databind.node.JsonNodeType.NUMBER
import com.fasterxml.jackson.databind.node.JsonNodeType.OBJECT
import com.fasterxml.jackson.databind.node.JsonNodeType.STRING
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NumericNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import strikt.api.Assertion
import strikt.assertions.map

/**
 * Asserts that the subject node has a field named [fieldName].
 */
infix fun <T : JsonNode> Assertion.Builder<T>.has(fieldName: String): Assertion.Builder<T> =
  assert("has a field named '$fieldName'") { subject ->
    if (subject.has(fieldName)) {
      pass(actual = subject.fields().asSequence().map { it.key }.toList())
    } else {
      fail(actual = subject.fields().asSequence().map { it.key }.toList())
    }
  }

/**
 * Maps the subject node of the assertion builder to the node accessed by
 * [fieldName].
 *
 * @return an assertion builder whose subject is the named field. If the path is
 * invalid the subject of the returned assertion builder will be a
 * [com.fasterxml.jackson.databind.node.MissingNode].
 */
infix fun <T : JsonNode> Assertion.Builder<T>.path(fieldName: String): Assertion.Builder<JsonNode> =
  get("nodes at $fieldName") { path(fieldName) }

/**
 * Maps the subject node of the assertion builder to the node accessed by [pointer].
 *
 * @return an assertion builder whose subject is the node at [pointer]. If [pointer] is invalid the
 * subject of the returned assertion builder will be a
 * [com.fasterxml.jackson.databind.node.MissingNode].
 */
infix fun <T : JsonNode> Assertion.Builder<T>.at(pointer: String): Assertion.Builder<JsonNode> = get("nodes at $pointer") { at(pointer) }

/**
 * Maps the subject node of the assertion builder to the node accessed by [pointer].
 *
 * @return an assertion builder whose subject is the node at [pointer]. If [pointer] is invalid the
 * subject of the returned assertion builder will be a
 * [com.fasterxml.jackson.databind.node.MissingNode].
 */
infix fun <T : JsonNode> Assertion.Builder<T>.at(pointer: JsonPointer): Assertion.Builder<JsonNode> =
  get("nodes at $pointer") { at(pointer) }

/**
 * Asserts that the subject node is a JSON object.
 *
 * @return an assertion builder over the same subject that is now known to be
 * an [ObjectNode].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsonNode> Assertion.Builder<T>.isObject(): Assertion.Builder<ObjectNode> = hasNodeType(OBJECT) as Assertion.Builder<ObjectNode>

/**
 * Asserts that the subject node is a JSON array.
 *
 * @return an assertion builder over the same subject that is now known to be
 * an [ArrayNode].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsonNode> Assertion.Builder<T>.isArray(): Assertion.Builder<ArrayNode> = hasNodeType(ARRAY) as Assertion.Builder<ArrayNode>

/**
 * Asserts that the subject node is a JSON text node.
 *
 * @return an assertion builder over the same subject that is now known to be
 * a [TextNode].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsonNode> Assertion.Builder<T>.isTextual(): Assertion.Builder<TextNode> = hasNodeType(STRING) as Assertion.Builder<TextNode>

/**
 * Asserts that the subject node is a JSON numeric node.
 *
 * @return an assertion builder over the same subject that is now known to be
 * a [NumericNode].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsonNode> Assertion.Builder<T>.isNumber(): Assertion.Builder<NumericNode> = hasNodeType(NUMBER) as Assertion.Builder<NumericNode>

/**
 * Asserts that the subject node is a JSON boolean node.
 *
 * @return an assertion builder over the same subject that is now known to be
 * a [BooleanNode].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsonNode> Assertion.Builder<T>.isBoolean(): Assertion.Builder<BooleanNode> = hasNodeType(BOOLEAN) as Assertion.Builder<BooleanNode>

/**
 * Asserts that the subject node is a JSON missing node.
 *
 * @return an assertion builder over the same subject that is now known to be
 * a [MissingNode].
 */
@Suppress("UNCHECKED_CAST")
fun <T : JsonNode> Assertion.Builder<T>.isMissing(): Assertion.Builder<MissingNode> = hasNodeType(MISSING) as Assertion.Builder<MissingNode>

/**
 * Maps the assertion to an assertion on the subject node's text value.
 *
 * Be aware that if the node is not a text node this will map to an assertion on
 * `null`.
 *
 * @see isTextual
 */
fun <T : JsonNode> Assertion.Builder<T>.textValue(): Assertion.Builder<String?> = get("text value", JsonNode::textValue)

/**
 * Maps the assertion to an assertion on the subject node's number value.
 *
 * Be aware that if the node is not a numeric node this will map to an assertion
 * on `null`.
 *
 * @see isNumber
 */
fun <T : JsonNode> Assertion.Builder<T>.numberValue(): Assertion.Builder<Number?> = get("number value", JsonNode::numberValue)

/**
 * Maps the assertion to an assertion on the subject node's boolean value.
 *
 * Be aware that if the node is not a boolean node this will map to an assertion
 * on `false`.
 *
 * @see isBoolean
 */
fun <T : JsonNode> Assertion.Builder<T>.booleanValue(): Assertion.Builder<Boolean> = get("boolean value", JsonNode::booleanValue)

/**
 * Maps the assertion to an assertion on the text values of [fieldName] in each
 * child of the current node.
 *
 * @see JsonNode.findValuesAsText
 */
fun <T : JsonNode> Assertion.Builder<T>.findValuesAsText(fieldName: String): Assertion.Builder<List<String>> =
  get("text values of $fieldName") { findValuesAsText(fieldName) }

/**
 * Asserts that the subject node's [JsonNode.getNodeType] method returns
 * [nodeType].
 *
 * Convenient aliases for this assertion exist for common node types that also
 * narrow the subject type of the resulting assertion builder.
 *
 * @see isObject
 * @see isArray
 * @see isTextual
 * @see isNumber
 * @see isBoolean
 */
infix fun <T : JsonNode> Assertion.Builder<T>.hasNodeType(nodeType: JsonNodeType): Assertion.Builder<T> =
  assert("is a $nodeType node", nodeType) {
    when (it.nodeType) {
      nodeType -> pass(actual = it.nodeType, description = "found a %s node")
      else -> fail(actual = it.nodeType, description = "found a %s node")
    }
  }

/**
 * Asserts that a JSON array's size is equal to [expected].
 *
 * @see ArrayNode.size
 */
infix fun Assertion.Builder<ArrayNode>.hasSize(expected: Int): Assertion.Builder<ArrayNode> =
  assert("has $expected elements", expected) { subject ->
    if (subject.size() == expected) {
      pass(actual = subject.size())
    } else {
      fail(actual = subject.size())
    }
  }

/**
 * Maps an assertion on a JSON array to an assertion on its size.
 *
 * @see ArrayNode.size
 */
fun Assertion.Builder<ArrayNode>.size(): Assertion.Builder<Int> = get("size", ArrayNode::size)

/**
 * Maps an assertion on a JSON array to an assertion on the text values of all nodes in the array.
 */
fun Assertion.Builder<ArrayNode>.textValues(): Assertion.Builder<Iterable<String>> = map { it.textValue() }

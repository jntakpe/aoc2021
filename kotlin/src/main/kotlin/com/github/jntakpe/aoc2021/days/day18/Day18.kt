package com.github.jntakpe.aoc2021.days.day18

import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputLines

object Day18 : Day {

    override val input = readInputLines(18)

    override fun part1() = input.map(::parseNode).reduce(Node::plus).magnitude()

    override fun part2(): Number {
        return input.indices.flatMap { i -> input.indices.map { j -> i to j } }
            .filter { (i, j) -> i != j }
            .maxOf { (i, j) -> (parseNode(input[i]) + parseNode(input[j])).magnitude() }
    }

    private fun parseNode(input: String): Node {
        var i = 0
        fun parse(parent: NodePair): Node {
            return if (input[i++] == '[') {
                NodePair(null, null, parent).apply {
                    left = parse(this).also { i++ }
                    right = parse(this).also { i++ }
                }
            } else NodeNumber(input[i - 1].digitToInt(), parent)
        }
        return parse(NodePair(null, null))
    }

    sealed class Node(var parent: NodePair? = null) {

        abstract fun magnitude(): Int

        operator fun plus(other: Node) = NodePair(this, other).apply { reduce() }

        fun reduce() {
            toExplode()?.apply {
                leftNode()?.also { it.value = it.value + (left as NodeNumber).value }
                rightNode()?.also { it.value = it.value + (right as NodeNumber).value }
                parent?.update(this, NodeNumber(0))
                this@Node.reduce()
            }
            toSplit()?.apply {
                parent?.update(this, NodePair(NodeNumber(value / 2), NodeNumber((value + 1) / 2)))
                this@Node.reduce()
            }
        }

        fun leftNode(): NodeNumber? = when (this) {
            is NodeNumber -> this
            parent?.left -> parent?.leftNode()
            parent?.right -> parent?.left?.farRightNode()
            else -> null
        }

        fun rightNode(): NodeNumber? = when (this) {
            is NodeNumber -> this
            parent?.left -> parent?.right?.farLeftNode()
            parent?.right -> parent?.rightNode()
            else -> null
        }

        private fun farLeftNode(): NodeNumber? = when (this) {
            is NodeNumber -> this
            is NodePair -> left?.farLeftNode()
        }

        private fun farRightNode(): NodeNumber? = when (this) {
            is NodeNumber -> this
            is NodePair -> right?.farRightNode()
        }

        private fun toExplode(depth: Int = 0): NodePair? = when (this) {
            is NodePair -> takeIf { depth >= 4 && left is NodeNumber && right is NodeNumber } ?: left?.toExplode(depth + 1)
            ?: right?.toExplode(depth + 1)
            else -> null
        }

        private fun toSplit(): NodeNumber? = when (this) {
            is NodeNumber -> takeIf { value >= 10 }
            is NodePair -> left?.toSplit() ?: right?.toSplit()
        }
    }

    open class NodePair(var left: Node?, var right: Node?, parent: NodePair? = null) : Node(parent) {
        init {
            left?.parent = this
            right?.parent = this
        }

        override fun magnitude() = 3 * left!!.magnitude() + 2 * right!!.magnitude()

        fun update(child: Node, newValue: Node) {
            if (left == child) {
                left = newValue
            } else if (right == child) {
                right = newValue
            }
            newValue.parent = this
        }
    }

    class NodeNumber(var value: Int, parent: NodePair? = null) : Node(parent) {

        override fun magnitude() = value
    }
}

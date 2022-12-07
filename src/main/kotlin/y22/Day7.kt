package y22

import puzzle

/**
 * [AOC 2022 Day 7](https://adventofcode.com/2022/day/7)
 */
fun main() = puzzle(2022, 7) {
    val parentNode = DirectoryNode("/")
    var currentDir = parentNode
    inputLines.forEach { line ->
        when {
            line.startsWith("$ cd") -> {
                val dir = line.substringAfter("cd ")
                currentDir = when (dir) {
                    "/" -> parentNode
                    ".." -> currentDir.parent as DirectoryNode
                    else -> currentDir.children.first { it.name == dir } as DirectoryNode
                }
            }

            line == "$ ls" -> {}
            line.startsWith("dir") -> currentDir.addChild(DirectoryNode(line.substringAfter("dir ")))
            else -> currentDir.addChild(FileNode(line.substringAfter(" "), line.substringBefore(" ").toInt()))
        }
    }

    submit {
        parentNode.getAllChildDirectories().filter { it.size <= 100000 }.sumOf { it.size }
    }


    submit {
        val neededSpace = 30000000 - (70000000 - parentNode.size)
        parentNode.getAllChildDirectories().filter { it.size >= neededSpace }.minBy { it.size }.size
    }
}

sealed class Node(val name: String, internal var parent: DirectoryNode?) {
    abstract val size: Int
}

class DirectoryNode(name: String) : Node(name, null) {
    private var _children: MutableList<Node> = mutableListOf()
    val children: List<Node> get() = _children

    override val size: Int
        get() = _children.sumOf {
            if (it is FileNode) it.size else (it as DirectoryNode).size
        }

    fun addChild(child: Node) {
        _children.add(child)
        child.parent = this
    }

    override fun toString(): String {
        return "y22.DirectoryNode(name='$name', children=$children)"
    }

    fun getAllChildDirectories(): List<DirectoryNode> {
        return _children.filterIsInstance<DirectoryNode>() + _children.filterIsInstance<DirectoryNode>()
            .flatMap { it.getAllChildDirectories() }
    }
}

class FileNode(name: String, override val size: Int) : Node(name, null) {
    override fun toString(): String {
        return "y22.FileNode(name='$name', size=$size)"
    }
}

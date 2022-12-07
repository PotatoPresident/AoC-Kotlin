/**
 * [AOC 2022 Day 7](https://adventofcode.com/2022/day/7)
 */
fun main() = puzzle(2022, 7) {
    val parentNode = DirectoryNode("/")
    var currentDir = parentNode
    inputLines.forEach { line ->
        if (line.startsWith("$")) {
            val cmd = line.split(' ')[1]
            if (cmd == "cd") {
                val dir = line.split(' ')[2]
                when (dir) {
                    "/" -> currentDir = parentNode
                    ".." -> currentDir = currentDir.parent as DirectoryNode
                    else -> currentDir = currentDir.children.first { it.name == dir } as DirectoryNode
                }
            }
        }

        if (line.startsWith("dir")) {
            val dirName = line.split(' ')[1]
            val node = DirectoryNode(dirName)
            currentDir.addChild(node)
        }

        if (line.first().isDigit()) {
            val fileSize = line.split(' ')[0].toInt()
            val fileName = line.split(' ')[1]
            val node = FileNode(fileName, fileSize)
            currentDir.addChild(node)
        }
    }

    submit {
        parentNode.getChildDirectories().filter { it.size <= 100000 }.sumOf { it.size }
    }


    submit {
        val totalSpace = 70000000
        val updateSpace = 30000000
        val usedSpace = parentNode.size //47
        val unusedSpace = totalSpace - usedSpace


        val neededSpace = updateSpace - unusedSpace


        val node = parentNode.getChildDirectories().filter { it.size >= neededSpace }.minBy { it.size }
        println("Needed Space $neededSpace. ${node.size}")
        node.name
    }
}

abstract class Node(val name: String, var parent: Node?)

class DirectoryNode(name: String) : Node(name, null) {
    var children: MutableList<Node> = mutableListOf()
    val size: Int
        get() = children.sumOf {
            if (it is FileNode) it.size else (it as DirectoryNode).size
        }

    fun addChild(child: Node) {
        children.add(child)
        child.parent = this
    }

    override fun toString(): String {
        return "DirectoryNode(name='$name', children=$children)"
    }

    fun getChildDirectories(): List<DirectoryNode> {
        return children.filterIsInstance<DirectoryNode>() + children.filterIsInstance<DirectoryNode>()
            .flatMap { it.getChildDirectories() }
    }
}

class FileNode(name: String, val size: Int) : Node(name, null) {
    override fun toString(): String {
        return "FileNode(name='$name', size=$size)"
    }
}

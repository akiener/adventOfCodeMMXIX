import java.io.File
import java.lang.Exception
import kotlin.system.exitProcess
import kotlin.test.assertEquals

fun main() {
    var maximumOutput = Pair(Int.MIN_VALUE, listOf<Int>())
    for (a in 0 until 5) {
        for (b in 0 until 5) {
            for (c in 0 until 5) {
                for (d in 0 until 5) {
                    for (e in 0 until 5) {
                        if (listOf(a, b, c, d, e).toSet().size < 5) {
                            continue
                        }
                        var memoryA = mutableListOf<Int>()
                        var memoryB = mutableListOf<Int>()
                        var memoryC = mutableListOf<Int>()
                        var memoryD = mutableListOf<Int>()
                        var memoryE = mutableListOf<Int>()
                        var outputA = 0
                        var outputB = 0
                        var outputC = 0
                        var outputD = 0
                        var outputE = 0

                        for (x in 0..29) {
                            val resA = runAmplifier(memoryA, a + 5, outputE)
                            memoryA = resA.first
                            outputA = resA.second
                            val resB = runAmplifier(memoryB, b + 5, outputA)
                            memoryB = resB.first
                            outputB = resB.second
                            val resC = runAmplifier(memoryC, c + 5, outputB)
                            memoryC = resC.first
                            outputC = resC.second
                            val resD = runAmplifier(memoryD, d + 5, outputC)
                            memoryD = resD.first
                            outputD = resD.second
                            val resE = runAmplifier(memoryE, e + 5, outputD)
                            memoryE = resE.first
                            outputE = resE.second
                        }

                        println("possible output: ${outputE to listOf(a + 5, b + 5, c + 5, d + 5, e + 5)}")
                        if (outputE > maximumOutput.first) {
                            maximumOutput = outputE to listOf(a + 5, b + 5, c + 5, d + 5, e + 5)
                        }
                        exitProcess(0)
                    }
                }
            }
        }
    }

    println("part2: $maximumOutput")
}

private fun runAmplifier(inputMemory: MutableList<Int>, position: Int, input: Int): Pair<MutableList<Int>, Int> {
    val memory = if (inputMemory.isEmpty()) {
        val file = File("input/07test")
        val lines = file.readLines()

        assertEquals(lines.size, 1)

        lines[0].split(",").map { Integer.parseInt(it) }.toMutableList()
    } else {
        inputMemory
    }

    var i = 0
    var firstInputUsed = false
    while (i < memory.size) {
        val ins = memory[i]

        val opcode = ins % 100
        val m1 = ins / 100 % 10 == 1
        val m2 = ins / 1000 % 10 == 1
        val m3 = ins / 10000 % 10 == 1
//        println("instruction: ${memory[i]}")

        when (opcode) {
            1 -> { // Add
                memory.write(m3, i + 3, memory.read(m1, i + 1) + memory.read(m2, i + 2))
                i += 4
            }
            2 -> { // Multiply
                memory.write(m3, i + 3, memory.read(m1, i + 1) * memory.read(m2, i + 2))
                i += 4
            }
            3 -> { // Save input to position
                if (!firstInputUsed) {
                    memory.write(m1, i + 1, position)
                    firstInputUsed = true
                } else {
                    memory.write(m1, i + 1, input)
                }
                i += 2
            }
            4 -> { // Output from position
                val output = memory.read(m1, i + 1)
                return memory to output
                i += 2
            }
            5 -> {
                if (memory.read(m1, i + 1) != 0) {
                    i = memory.read(m2, i + 2)
                } else {
                    i += 3
                }
            }
            6 -> {
                if (memory.read(m1, i + 1) == 0) {
                    i = memory.read(m2, i + 2)
                } else {
                    i += 3
                }
            }
            7 -> {
                if (memory.read(m1, i + 1) < memory.read(m2, i + 2)) {
                    memory.write(m3, i + 3, 1)
                } else {
                    memory.write(m3, i + 3, 0)
                }
                i += 4
            }
            8 -> {
                if (memory.read(m1, i + 1) == memory.read(m2, i + 2)) {
                    memory.write(m3, i + 3, 1)
                } else {
                    memory.write(m3, i + 3, 0)
                }
                i += 4
            }
            99 -> {
                println(memory)
                println("terminated normally")
                exitProcess(0)
            }
            else -> throw Exception("operation $opcode unknown")
        }
    }

    println(memory)
    throw Exception("Did not expect program to run to here")
}

private fun MutableList<Int>.write(modeImmediate: Boolean, i: Int, value: Int) {
    if (modeImmediate) {
        this[i] = value
    } else {
        this[this[i]] = value
    }
}

private fun MutableList<Int>.read(modeImmediate: Boolean, i: Int): Int {
    return if (modeImmediate) {
        this[i]
    } else {
        this[this[i]]
    }
}
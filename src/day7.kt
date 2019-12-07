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
                        val outputA = runAmplifier(a, 0)
                        val outputB = runAmplifier(b, outputA)
                        val outputC = runAmplifier(c, outputB)
                        val outputD = runAmplifier(d, outputC)
                        val outputE = runAmplifier(e, outputD)
                        println("possible output: $outputE")
                        if (outputE > maximumOutput.first) {
                            maximumOutput = outputE to listOf(a, b, c, d, e)
                        }
                    }
                }
            }
        }
    }

    println("part1: $maximumOutput")
}

private fun runAmplifier(position: Int, input: Int): Int {
    val file = File("input/07")
    val lines = file.readLines()

    assertEquals(lines.size, 1)

    val memory = lines[0].split(",").map { Integer.parseInt(it) }.toMutableList()

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
                return output
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
import java.io.File
import java.lang.Exception
import kotlin.test.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main() = runBlocking {
    var maximumOutput = Pair(Int.MIN_VALUE, listOf<Int>())
    for (a in 0 until 5) {
        for (b in 0 until 5) {
            for (c in 0 until 5) {
                for (d in 0 until 5) {
                    for (e in 0 until 5) {
                        if (listOf(a, b, c, d, e).toSet().size < 5) {
                            continue
                        }

                        val channelAtoB = Channel<Int>()
                        val channelBtoC = Channel<Int>()
                        val channelCtoD = Channel<Int>()
                        val channelDtoE = Channel<Int>()
                        val channelEtoA = Channel<Int>()

                        val ampA = launch {
                            runAmplifier(a + 5, channelEtoA, channelAtoB)
                        }
                        val ampB = launch {
                            runAmplifier(b + 5, channelAtoB, channelBtoC)
                        }
                        val ampC = launch {
                            runAmplifier(c + 5, channelBtoC, channelCtoD)
                        }
                        val ampD = launch {
                            runAmplifier(d + 5, channelCtoD, channelDtoE)
                        }
                        val ampE = launch {
                            runAmplifier(e + 5, channelDtoE, channelEtoA)
                        }


                        GlobalScope.launch {
                            channelEtoA.send(0)
                        }

                        ampA.join()
                        ampB.join()
                        ampC.join()
                        ampD.join()
                        val finalOutput = channelEtoA.receive()
                        ampE.join()

//                        println("possible output: ${finalOutput to listOf(a + 5, b + 5, c + 5, d + 5, e + 5)}")
                        if (finalOutput > maximumOutput.first) {
                            maximumOutput = finalOutput to listOf(a + 5, b + 5, c + 5, d + 5, e + 5)
                        }
                    }
                }
            }
        }
    }

    println("part2: $maximumOutput")
}

private suspend fun runAmplifier(position: Int, channelInput: Channel<Int>, channelOutput: Channel<Int>) {
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
                    val input = channelInput.receive()
//                    println("$position recv $input")
                    memory.write(m1, i + 1, input)
                }
                i += 2
            }
            4 -> { // Output from position
                val output = memory.read(m1, i + 1)
//                println("$position send $output")
                channelOutput.send(output)
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
//                println(memory)
//                println("amplifier $position terminated normally")
                return
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
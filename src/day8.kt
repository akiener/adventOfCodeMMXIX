import java.io.File
import kotlin.test.assertEquals

fun main() {
    val file = File("input/08")
    val lines = file.readLines()

    assertEquals(lines.size, 1)

    val input = lines[0].toCharArray().map { Integer.parseInt("$it") }

    val maxWidth = 25
    val maxHeight = 6
    val layerSize = maxWidth * maxHeight

    var i = 0

    val layers = mutableListOf<MutableList<Int>>()
    while (i < input.size) {
        val layer = mutableListOf<Int>()
        for (height in 0 until maxHeight) {
            for (width in 0 until maxWidth) {
                layer.add(input[i])
                i++
            }
        }
        layers.add(layer)
    }

    val layerWithFewest0 =
        layers.map { layer -> layer.count { pixel -> pixel == 0 } to layer }.minBy { it.first }?.second!!
    val result = layerWithFewest0.count { it == 1 } * layerWithFewest0.count { it == 2 }

    println("part1: $result")

    val image = mutableListOf<MutableList<Int>>()
    for (height in 0 until maxHeight) {
        val imageLine = mutableListOf<Int>()
        for (width in 0 until maxWidth) {
            var pixelColor = 2
            for (layer in layers) {
                val pixelColorAtLayer = layer[width + maxWidth * height]
                if (pixelColor == 2) {
                    pixelColor = pixelColorAtLayer
                }
            }
            imageLine.add(pixelColor)
        }
        image.add(imageLine)
    }

    println("part2:")
    for (imageLine in image) {
        imageLine.map {
            print(when (it) {
                0 -> " "
                1 -> "O"
                2 -> "_"
                else -> "X"
            })
        }
        println()
    }
}
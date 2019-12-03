package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Direction string

type DirectionFunction func(distance int) (int, int)

const (
	U Direction = "U"
	R Direction = "R"
	D Direction = "D"
	L Direction = "L"
)

func up(distance int) (int, int) {
	return 0, distance
}

func right(distance int) (int, int) {
	return distance, 0
}

func down(distance int) (int, int) {
	return 0, -distance
}

func left(distance int) (int, int) {
	return -distance, 0
}

type Coordinate struct {
	x int
	y int
}

type CoordinateLine struct {
	from Coordinate
	to   Coordinate
}

func San() {
	file, _ := os.Open("input/san.txt")
	defer file.Close()

	dirs := map[Direction]DirectionFunction{
		U: up,
		R: right,
		D: down,
		L: left,
	}

	lines := make([][]string, 2)
	scanner := bufio.NewScanner(file)
	for i := 0; i < 2; i++ {
		lines[i] = make([]string, 0, 200)
		scanner.Scan()
		input := scanner.Text()
		split := strings.Split(input, ",")
		for _, v := range split {
			lines[i] = append(lines[i], v)
		}
	}

	grid := map[Coordinate]int{
		Coordinate{
			x: 0,
			y: 0,
		}: 1 + 2,
	}
	gridLines := make([][]CoordinateLine, 2)

	for i, line := range lines {
		pos := Coordinate{
			x: 0,
			y: 0,
		}
		gridLines[i] = make([]CoordinateLine, 0, 200)
		for _, move := range line {
			//lineId := i + 1
			runes := []rune(move)

			distance, _ := strconv.Atoi(string(runes[1:]))
			x, y := dirs[Direction(runes[0:1])](distance)
			prevPos := pos
			pos.x += x
			pos.y += y

			gridLines[i] = append(gridLines[i], CoordinateLine{
				from: prevPos,
				to:   pos,
			})

			//if _, ok := grid[pos]; ok {
			//	grid[pos] += lineId
			//} else {
			//	grid[pos] = lineId
			//}
		}
	}

	for i := 0; i < len(gridLines[0]); i++ {
		one := gridLines[0][i]
		for j, _ := range gridLines[1] {
			two := gridLines[1][j]
			if i, c := intersects(one, two); i {
				grid[c] = 1 + 2
			}
		}
	}

	for i := 0; i < len(gridLines[1]); i++ {

	}

	manhattenDistances := map[Coordinate]int{}

	for k, v := range grid {
		if v == 3 {
			manhattenDistances[k] = manhattenDistance(k)
			fmt.Println(k)
		}
	}

	print(dirs)

	{
	}

	{
	}
}

func manhattenDistance(pos Coordinate) int {

}

func intersects(one CoordinateLine, two CoordinateLine) (bool, Coordinate) {
	if one.from.x < one.to.x && one.from.x < two.from.x && two.to.x < one.to.x && two.from.y < one.from.y && one.to.y < two.to.y { // one - two |
		return true, Coordinate{
			x: two.from.x,
			y: one.from.y,
		}
	}
	if two.from.x < two.to.x && two.from.x < one.from.x && one.to.x < two.to.x && one.from.y < two.from.y && two.to.y < one.to.y { // one | two -
		return true, Coordinate{
			x: one.from.x,
			y: two.from.y,
		}
	}
	return false, Coordinate{}
}

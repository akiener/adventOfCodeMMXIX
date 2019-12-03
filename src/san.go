package main

import (
	"fmt"
	"strconv"
	"strings"
)

type Pos struct {
	x int
	y int
}

func San(input string) int {
	lines := strings.Split(input, "\n")
	movements := make([][]string, 2)
	for i := 0; i < 2; i++ {
		movements[i] = make([]string, 0, 200)
		split := strings.Split(lines[i], ",")
		for _, v := range split {
			movements[i] = append(movements[i], v)
		}
	}

	paths := map[int][]Pos{
		0: make([]Pos, 0, 1024),
		1: make([]Pos, 0, 1024),
	}

	for i, moves := range movements {
		paths[i] = append(paths[i], Pos{})
		currentPos := Pos{}

		for moveIndex, move := range moves {
			runes := []rune(move)

			distance, _ := strconv.Atoi(string(runes[1:]))
			switch string(runes[0:1]) {
			case "U":
				paths[i] = append(paths[i], Pos{currentPos.x, currentPos.y + distance})
			case "R":
				paths[i] = append(paths[i], Pos{currentPos.x + distance, currentPos.y})
			case "D":
				paths[i] = append(paths[i], Pos{currentPos.x, currentPos.y - distance})
			case "L":
				paths[i] = append(paths[i], Pos{currentPos.x - distance, currentPos.y})
			}
			currentPos = paths[i][moveIndex+1]
		}
	}

	intersections := make([]Pos, 0)

	prev0 := Pos{}
	for _, move0 := range paths[0] {

		prev1 := Pos{}
		for _, move1 := range paths[1] {
			if prev0.x != move0.x &&
				prev1.y != move1.y &&
				((prev0.x < move1.x && move1.x < move0.x) || (prev0.x > move1.x && move1.x > move0.x)) &&
				((prev1.y < move0.y && move0.y < move1.y) || (prev1.y > move0.y && move0.y > move1.y)) {
				intersections = append(intersections, Pos{
					x: move1.x,
					y: move0.y,
				})
			} else if prev0.y != move0.y &&
				prev1.x != move1.x &&
				((prev0.y < move1.y && move1.y < move0.y) || (prev0.y > move1.y && move1.y > move0.y)) &&
				((prev1.x < move0.x && move0.x < move1.x) || (prev1.x > move0.x && move0.x > move1.x)) {
				intersections = append(intersections, Pos{
					x: move1.y,
					y: move0.x,
				})
			}

			prev1 = move1
		}

		prev0 = move0
	}

	minManhattenDistance := int(^uint(0) >> 1)

	for _, intersection := range intersections {
		manhattenDistance := abs(intersection.x) + abs(intersection.y)

		if manhattenDistance < minManhattenDistance {
			minManhattenDistance = manhattenDistance
		}
	}

	fmt.Println("part1:", minManhattenDistance)

	return minManhattenDistance
}

func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

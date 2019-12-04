package main

import "fmt"

func Yon() {
	var min = 382345
	var max = 843167

	var countValid = 0
	for i := min; i <= max; i++ {
		arr := []int{
			i / 100000,
			i / 10000 % 10,
			i / 1000 % 10,
			i / 100 % 10,
			i / 10 % 10,
			i % 10,
		}

		valid := adjacentMatch(arr) && digitsRise(arr) && is6DigitNumber(arr)
		if valid {
			countValid++
		}
	}

	fmt.Println("part1:", countValid)
}

func adjacentMatch(p []int) bool {
	for i := 0; i < len(p)-1; i++ {
		if p[i] == p[i+1] {
			return true
		}
	}
	return false
}

func digitsRise(p []int) bool {
	for i := 0; i < len(p)-1; i++ {
		if p[i] > p[i+1] {
			return false
		}
	}
	return true
}

func is6DigitNumber(p []int) bool {
	return p[0] != 0
}

func pow(input int, pow int) int {
	x := input
	for i := 0; i < pow; i++ {
		x *= input
	}
	return x
}

package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
)

func Ichi() {
	file, _ := os.Open("input/ichi.txt")
	defer file.Close()

	scanner := bufio.NewScanner(file)

	modules := [100]int{}
	for i := 0; scanner.Scan(); i++ {
		mass, _ := strconv.Atoi(scanner.Text())
		modules[i] = mass
	}

	{
		fuelRequired := 0
		for _, module := range modules {
			fuelRequired += calculateFuelForMassOnly(module)
		}
		fmt.Println("part 1:", fuelRequired)
	}

	{
		fuelRequired := 0
		for _, module := range modules {
			fuelRequired += calculateFuel(module)
		}
		fmt.Println("part 2:", fuelRequired)
	}
}

func calculateFuelForMassOnly(mass int) int {
	fuel := int(math.Floor(float64(mass/3)) - 2)
	// ignore negative fuel consumption (pack a bit extra, just to be sure. I am certain NASA also does it this way)
	if fuel <= 0 {
		return 0
	}
	return fuel
}

func calculateFuel(mass int) int {
	fuelRequired := 0
	for {
		fuel := calculateFuelForMassOnly(mass)
		fuelRequired += fuel
		if fuel <= 0 {
			return fuelRequired
		}
		mass = fuel
	}
}

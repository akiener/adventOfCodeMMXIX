package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Instruction int

const (
	Add Instruction = 1
	Mul Instruction = 2
	End Instruction = 99
)

func Ni() {
	file, _ := os.Open("input/ni.txt")
	defer file.Close()

	scanner := bufio.NewScanner(file)
	scanner.Scan()
	input := scanner.Text()

	split := strings.Split(input, ",")

	initialMemory := make([]int, len(split))
	for i, value := range split {
		initialMemory[i], _ = strconv.Atoi(value)
	}

	memory := make([]int, len(initialMemory))

	{
		copy(memory, initialMemory)
		result := runComputer(memory, 12, 2)
		fmt.Println("part 1:", result)
	}

	{
		for noun := 0; noun < 100; noun++ {
			for verb := 0; verb < 100; verb++ {
				copy(memory, initialMemory)
				result := runComputer(memory, noun, verb)
				if result == 19690720 {
					fmt.Println("part 2:", 100*noun+verb)
				}
			}
		}
	}
}

func runComputer(memory []int, noun, verb int) int {
	memory[1] = noun
	memory[2] = verb

	c := 0 // cursor
	for c < len(memory) {
		method := Instruction(memory[c])

		isEnd := false
		switch method {
		case Add:
			memory[memory[c+3]] = memory[memory[c+1]] + memory[memory[c+2]]
		case Mul:
			memory[memory[c+3]] = memory[memory[c+1]] * memory[memory[c+2]]
		case End:
			isEnd = true
		}
		if isEnd {
			break
		}

		c += 4

	}

	return memory[0]
}

package main

import (
	"bufio"
	"log"
	"os"
	"strconv"
	"strings"
)

type Node struct {
	value int

	up    *Node
	right *Node
	down  *Node
	left  *Node
}

func San2() {
	file, _ := os.Open("input/san.txt")
	defer file.Close()

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

	root := Node{}

	for i, line := range lines {
		lineValue := i + 1
		node := &root
		for _, move := range line {
			runes := []rune(move)

			distance, _ := strconv.Atoi(string(runes[1:]))
			switch string(runes[0:1]) {
			case "U":
				if node.up != nil {
					log.Fatal("up can not be set")
				}
				for d := 0; d < distance; d++ {
					node.up = &Node{
						value: lineValue,
						down:  node,
					}
					node = node.up
				}
			case "R":
				if node.right != nil {
					log.Fatal("right can not be set")
				}
				for d := 0; d < distance; d++ {
					node.right = &Node{
						value: lineValue,
						left:  node,
					}
					node = node.right
				}
			case "D":
				if node.down != nil {
					log.Fatal("down can not be set")
				}
				for d := 0; d < distance; d++ {
					node.down = &Node{
						value: lineValue,
						up:    node,
					}
					node = node.down
				}
			case "L":
				if node.left != nil {
					log.Fatal("left can not be set")
				}
				for d := 0; d < distance; d++ {
					node.left = &Node{
						value: lineValue,
						right: node,
					}
					node = node.left
				}
			}
		}

		print(i)
		print(line)
	}

}

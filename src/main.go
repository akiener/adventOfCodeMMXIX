package main

import (
	"bufio"
	"os"
)

func main() {
	switch problemIndex := 3; problemIndex {
	case 1:
		Ichi()
	case 2:
		Ni()
	case 3:
		file, _ := os.Open("input/san.txt")
		defer file.Close()
		lines := ""
		scanner := bufio.NewScanner(file)
		for i := 0; i < 2; i++ {
			scanner.Scan()
			lines += scanner.Text()
			lines += "\n"
		}
		San(lines)
	}
}

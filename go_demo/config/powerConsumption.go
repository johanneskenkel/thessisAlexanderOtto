package config

import (
	"log"
	"os/exec"
	"strconv"
	"strings"
	"time"
)

type PowerConsumption struct {
	pkgPowerConsumption  float64
	pp0PowerConsumption  float64
	dramPowerConsumption float64
}

func (pC PowerConsumption) PowerConsumption() (float64, float64, float64) {
	return pC.pkgPowerConsumption, pC.pp0PowerConsumption, pC.dramPowerConsumption
}

func twoToPowerOf(value int) int {
	return 1 << value
}

func extractBits(data int, lo int, hi int) int {
	return (data & (^0 << lo) & ^(^0<<hi + 1)) >> lo
}

func getConversionPowerUnit() float64 {
	command := exec.Command("/bin/bash", "-c", "rdmsr -d 0x606")
	stdout, err := command.Output()
	if err != nil {
		log.Default().Println(err)
		return -1.0
	}
	powerUnit, err := strconv.Atoi(strings.TrimRight(string(stdout), "\r\n"))
	if err != nil {
		log.Default().Println(err)
		return -1.0
	}

	return 1.0 / float64(twoToPowerOf(extractBits(powerUnit, 8, 12)))
}

func parseFloat(str string) float64 {
	value, err := strconv.ParseFloat(str, 64)
	if err != nil {
		log.Default().Println(err)
		return -1.0
	}
	return value
}

func StartReadingPowerConsumptionValues(powerConsumption *PowerConsumption) {
	conversionPowerUnit := getConversionPowerUnit()
	previousPkgEnergy := 0.0
	actualPkgEnergy := 0.0
	previousPP0Energy := 0.0
	actualPP0Energy := 0.0
	previousDRAMEnergy := 0.0
	actualDRAMEnergy := 0.0
	for {
		command := exec.Command("/bin/bash", "-c", "rdmsr -d 0x611; rdmsr -d 0x639; rdmsr -d 0x619;")
		stdout, err := command.Output()
		if err != nil {
			log.Default().Println(err)
			return
		}
		powerConsumptionStats := strings.Split(strings.TrimRight(string(stdout), "\r\n"), "\n")
		actualPkgEnergy = parseFloat(powerConsumptionStats[0]) * conversionPowerUnit
		if previousPkgEnergy != 0.0 {
			powerConsumption.pkgPowerConsumption = actualPkgEnergy - previousPkgEnergy
		}
		actualPP0Energy = parseFloat(powerConsumptionStats[1]) * conversionPowerUnit
		if previousPP0Energy != 0.0 {
			powerConsumption.pp0PowerConsumption = actualPP0Energy - previousPP0Energy
		}
		actualDRAMEnergy = parseFloat(powerConsumptionStats[2]) * conversionPowerUnit
		if previousDRAMEnergy != 0.0 {
			powerConsumption.dramPowerConsumption = actualDRAMEnergy - previousDRAMEnergy
		}

		previousPkgEnergy = actualPkgEnergy
		previousPP0Energy = actualPP0Energy
		previousDRAMEnergy = actualDRAMEnergy

		time.Sleep(1 * time.Second)
	}
}

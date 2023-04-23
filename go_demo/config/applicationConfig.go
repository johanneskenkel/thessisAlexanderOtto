package config

import (
	"encoding/json"
	"io/ioutil"
	"log"
	"os"
)

type ApplicationConfig struct {
	Postgres Postgres `json:"postgres"`
}

type Postgres struct {
	Url   string `json:"url"`
	Table string `json:"table"`
}

// currently unused, because it breaks the tests
func GetApplicationConfig() *ApplicationConfig {
	configurationFile, err := os.Open("config/applicationConfig.json")
	if err != nil {
		log.Fatal(err)
	}
	defer configurationFile.Close()
	configurationBytes, err := ioutil.ReadAll(configurationFile)
	if err != nil {
		panic(err)
	}
	configuration := ApplicationConfig{}
	err = json.Unmarshal(configurationBytes, &configuration)
	if err != nil {
		log.Fatal(err)
	}
	return &configuration
}

package main

import (
	"github.com/SanyaNooB/thesis.git/models"
	"github.com/SanyaNooB/thesis.git/routers"
)

func main() {
	models.InitDB()
	routers.CreateUrlMappings()
	routers.Router.Run(":8082")
}

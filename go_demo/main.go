package main

import (
	"github.com/SanyaNooB/thesis.git/routers"
)

func main() {
	routers.CreateUrlMappings()
	routers.Router.Run(":8082")
}

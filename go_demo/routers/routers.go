package routers

import (
	"github.com/SanyaNooB/thesis.git/api"
	"github.com/gin-gonic/gin"
)

var Router *gin.Engine

func CreateUrlMappings() {
	Router = gin.Default()
	v1 := Router.Group("/api/persons")
	{
		v1.GET("/", api.GetPersons)
		v1.GET("/:id", api.GetPersonById)
		v1.POST("/insert", api.InsertPerson)
		v1.POST("/generate_random", api.GenerateRandomPersons)
		v1.DELETE("/delete/table", api.RemoveDBTable)
		v1.PUT("/update", api.UpdatePerson)
	}
}

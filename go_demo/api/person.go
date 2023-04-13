package api

import (
	"log"
	"strconv"

	"github.com/SanyaNooB/thesis.git/models"
	"github.com/gin-gonic/gin"
)

func GetPersons(c *gin.Context) {
	var err error
	numberOfPersons := c.Query("numberOfPersons")
	var persons []*models.Person
	if isNumber(numberOfPersons) {
		persons, err = models.GetNumberOfPersons(numberOfPersons)
	} else {
		persons, err = models.GetAllPersons()
	}
	if err != nil {
		c.JSON(404, "Couldn't get persons")
		log.Default().Println(err)
	} else {
		c.JSON(200, persons)
	}
}

func GetPersonById(c *gin.Context) {
	personId := c.Param("id")
	var person *models.Person
	var err error
	if isNumber(personId) {
		person, err = models.GetPersonById(personId)
	} else {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
		return
	}
	if err != nil {
		c.JSON(404, "Something went wrong"+personId)
		log.Default().Println(err)
	} else {
		c.JSON(200, person)
	}
}

func InsertPerson(c *gin.Context) {
	var person models.Person
	err := c.BindJSON(&person)
	if err != nil {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
		return
	}
	message, err := models.InsertPersonToDb(&person)
	if err != nil {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
	} else {
		c.JSON(200, message)
	}
}

type generatePersons struct {
	NumberOfPersonsToGenerate int64 `json:"numberOfPersons"`
}

func GenerateRandomPersons(c *gin.Context) {
	var generatePersons generatePersons
	err := c.BindJSON(&generatePersons)
	if err != nil {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
		return
	}
	message, err := models.GenerateNumberOfRandomPersonsToDB(generatePersons.NumberOfPersonsToGenerate)
	if err != nil {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
	} else {
		c.JSON(200, message)
	}
}

func RemoveDBTable(c *gin.Context) {
	message, err := models.DropDBTable()
	if err != nil {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
	} else {
		c.JSON(200, message)
	}
}

func UpdatePerson(c *gin.Context) {
	var person models.Person
	if err := c.BindJSON(&person); err != nil {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
		return
	}
	message, err := models.UpdatePerson(&person)
	if err != nil {
		c.JSON(400, "Something went wrong")
		log.Default().Println(err)
	} else {
		c.JSON(200, message)
	}
}

func isNumber(str string) bool {
	_, err := strconv.Atoi(str)
	if err != nil {
		return false
	}
	return true
}

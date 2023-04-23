package models

import (
	"testing"
)

func TestThatThousandPersonsAreGenerated(t *testing.T) {
	InitDB()
	numberOfPersonsToGenerate := 1000
	GenerateNumberOfRandomPersonsToDB(int64(numberOfPersonsToGenerate))
	persons, _ := GetAllPersons()
	InitDB()
	t.Log(len(persons))
	defer DropDBTable()
	if numberOfPersonsToGenerate != len(persons) {
		t.Fatalf("TestThatThousandPersonsAreGenerated Failed")
	}
}

func TestThatPersonsGeneratedCorrectly(t *testing.T) {
	InitDB()
	numberOfPersonsToGenerate := 100
	GenerateNumberOfRandomPersonsToDB(int64(numberOfPersonsToGenerate))
	persons, _ := GetAllPersons()
	for _, person := range persons {
		if person.FirstName == "" || person.LastName == "" || person.BirthDate == "" || person.Timestamp.String() == "" || person.Id < 0 || person.Id > int64(numberOfPersonsToGenerate) {
			t.Fatalf("TestThatPersonsGeneratedCorrectly failed")
		}
	}
	defer DropDBTable()
}

func TestThatPersonInsertionWorksCorrectly(t *testing.T) {
	InitDB()
	GenerateNumberOfRandomPersonsToDB(100)
	person := &Person{Id: 0, FirstName: "John", LastName: "Test", BirthDate: "2007-06-17"}
	InsertPersonToDb(person)
	personFromDb, _ := GetPersonById("101")
	if personFromDb.FirstName != person.FirstName || personFromDb.LastName != person.LastName || personFromDb.BirthDate != "2007-06-17T00:00:00Z" || person.Timestamp.String() == "" {
		t.Fatalf("TestThatPersonInsertionWorksCorrectly failed")
	}
	defer DropDBTable()
}

func TestThatPersonUpdateWorksCorrectly(t *testing.T) {
	InitDB()
	GenerateNumberOfRandomPersonsToDB(10)
	person := &Person{Id: 7, FirstName: "John", LastName: "Test", BirthDate: "2007-06-17"}
	UpdatePerson(person)
	personFromDb, _ := GetPersonById("7")
	if personFromDb.FirstName != person.FirstName || personFromDb.LastName != person.LastName || personFromDb.BirthDate != "2007-06-17T00:00:00Z" || person.Timestamp.String() == "" {
		t.Fatalf("TestThatPersonUpdateWorksCorrectly failed")
	}
	defer DropDBTable()
}

func TestThatPersonIsDeletedCorrectlyById(t *testing.T) {
	InitDB()
	GenerateNumberOfRandomPersonsToDB(20)
	DeletePerson("20")
	DeletePerson("10")
	person20, _ := GetPersonById("20")
	person10, _ := GetPersonById("10")
	if person20 != nil || person10 != nil {
		t.Fatalf("TestThatPersonIsDeletedCorrectlyById")
	}
	defer DropDBTable()
}

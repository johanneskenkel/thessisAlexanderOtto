package models

import (
	"database/sql"

	_ "github.com/lib/pq"
)

var db *sql.DB

func InitDB() error {
	var err error
	db, err = sql.Open("postgres", "postgres://postgres:12345@localhost:5432/postgres?sslmode=disable")
	if err != nil {
		return err
	}
	db.SetMaxOpenConns(20)
	db.Query("CREATE TABLE IF NOT EXISTS persons (id SERIAL PRIMARY KEY, firstName VARCHAR(30), lastName VARCHAR(30), birthDate DATE, timestamp TIMESTAMP)")
	return db.Ping()
}

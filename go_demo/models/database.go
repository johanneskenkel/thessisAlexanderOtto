package models

import (
	"database/sql"

	"github.com/SanyaNooB/thesis.git/config"
	_ "github.com/lib/pq"
)

var db *sql.DB

func InitDB() error {
	var err error
	applicationConfig := config.GetApplicationConfig()
	db, err = sql.Open("postgres", applicationConfig.Postgres.Url)
	if err != nil {
		return err
	}
	db.SetMaxOpenConns(20)
	db.Query("CREATE TABLE IF NOT EXISTS " + applicationConfig.Postgres.Table +
		" (id SERIAL PRIMARY KEY, firstName VARCHAR(30), lastName VARCHAR(30), birthDate DATE, timestamp TIMESTAMP)")
	return db.Ping()
}

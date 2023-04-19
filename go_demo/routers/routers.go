package routers

import (
	"time"

	"github.com/SanyaNooB/thesis.git/api"
	"github.com/SanyaNooB/thesis.git/config"
	"github.com/gin-gonic/gin"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

var Router *gin.Engine

var (
	cpuPackagePowerConsumption = promauto.NewGauge(prometheus.GaugeOpts{Namespace: "golang_gin", Name: "cpu_package_power_consumption"})
	cpuPP0PowerConsumption     = promauto.NewGauge(prometheus.GaugeOpts{Namespace: "golang_gin", Name: "cpu_pp0_power_consumption"})
	cpuDramPowerConsumption    = promauto.NewGauge(prometheus.GaugeOpts{Namespace: "golang_gin", Name: "dram_power_consumption"})
)

func recordMetrics() {
	var powerConsumption config.PowerConsumption
	go config.StartReadingPowerConsumptionValues(&powerConsumption)
	go func() {
		for {
			pkg, pp0, dram := powerConsumption.PowerConsumption()
			if pkg >= 0 {
				cpuPackagePowerConsumption.Set(pkg)
			}
			if pp0 >= 0 {
				cpuPP0PowerConsumption.Set(pp0)
			}
			if dram >= 0 {
				cpuDramPowerConsumption.Set(dram)
			}
			time.Sleep(1 * time.Second)
		}
	}()
}

func CreateUrlMappings() {
	recordMetrics()

	Router = gin.New()

	v1 := Router.Group("/api/persons")
	metrics := Router.Group("/metrics")
	{
		v1.GET("/", api.GetPersons)
		v1.GET("/:id", api.GetPersonById)
		v1.POST("/insert", api.InsertPerson)
		v1.POST("/generate_random", api.GenerateRandomPersons)
		v1.PUT("/update", api.UpdatePerson)
		v1.DELETE("/delete/:id", api.DeletePersonById)
		v1.DELETE("/delete/table", api.RemoveDBTable)
		metrics.GET("", gin.WrapH(promhttp.Handler()))
	}
}

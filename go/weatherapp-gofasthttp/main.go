package main

import (
	"fmt"

	jsoniter "github.com/json-iterator/go"
	routing "github.com/qiangxue/fasthttp-routing"
	"github.com/valyala/fasthttp"
)

type weatherReport struct {
	Location string `json:"location"`
	Weather  string `json:"weather"`
}

func main() {
	router := routing.New()

	router.Get("/", func(c *routing.Context) error {
		c.SetContentType("application/json")
		report := weatherReport{"Seattle", "Cloudy"}
		var json = jsoniter.ConfigCompatibleWithStandardLibrary
		json.NewEncoder(c).Encode(report)
		return nil
	})

	fmt.Println("app listening on port 5000!")
	panic(fasthttp.ListenAndServe(":5000", router.HandleRequest))
}

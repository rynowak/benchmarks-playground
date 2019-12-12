package main

import (
	"fmt"
	"time"

	jsoniter "github.com/json-iterator/go"
	routing "github.com/qiangxue/fasthttp-routing"
	"github.com/valyala/fasthttp"
)

type weatherReport struct {
	Location string `json:"location"`
	Weather  string `json:"weather"`
}

var json jsoniter.API = jsoniter.ConfigFastest

func main() {
	router := routing.New()
	router.Get("/", func(c *routing.Context) error {
		c.SetContentType("application/json")
		json.NewEncoder(c).Encode(weatherReport{"Seattle", "Cloudy"})
		return nil
	})

	s := fasthttp.Server{
		TCPKeepalive:       true,
		TCPKeepalivePeriod: 30 * time.Second,
		Handler:            router.HandleRequest,
	}
	fmt.Println("app listening on port 5000!")
	panic(s.ListenAndServe(":5000"))
}

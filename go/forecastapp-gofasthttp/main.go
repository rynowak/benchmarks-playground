package main

import (
	"fmt"
	"time"

	"github.com/fasthttp/router"
	jsoniter "github.com/json-iterator/go"
	"github.com/valyala/fasthttp"
)

type weatherForecast struct {
	Weather string `json:"weather"`
}

var json = jsoniter.ConfigFastest

func forecast(ctx *fasthttp.RequestCtx) {

	time.Sleep(10 * time.Millisecond)
	ctx.SetContentType("application/json")
	json.NewEncoder(ctx).Encode(weatherForecast{"Cloudy"})
}

func main() {
	r := router.New()
	r.GET("/forecast", forecast)

	s := fasthttp.Server{
		TCPKeepalive:       true,
		TCPKeepalivePeriod: 30 * time.Second,
		Handler:            r.Handler,
		WriteTimeout:       5 * time.Second,
	}
	fmt.Println("app listening on port 8080!")
	panic(s.ListenAndServe(":8080"))
}

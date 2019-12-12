package main

import (
	"fmt"
	"time"

	"github.com/fasthttp/router"
	jsoniter "github.com/json-iterator/go"
	"github.com/valyala/fasthttp"
)

type weatherReport struct {
	Location string `json:"location"`
	Weather  string `json:"weather"`
}

var json jsoniter.API = jsoniter.ConfigFastest

func index(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("application/json")
	json.NewEncoder(ctx).Encode(weatherReport{"Seattle", "Cloudy"})
}

func main() {
	r := router.New()
	r.GET("/", index)

	s := fasthttp.Server{
		TCPKeepalive:       true,
		TCPKeepalivePeriod: 30 * time.Second,
		Handler:            r.Handler,
	}
	fmt.Println("app listening on port 5000!")
	panic(s.ListenAndServe(":5000"))
}

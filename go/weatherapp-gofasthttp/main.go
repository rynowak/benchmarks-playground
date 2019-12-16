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

func weather(ctx *fasthttp.RequestCtx) {

	_, body, err := fasthttp.Get(nil, "http://localhost:5000/forecast")
	if err != nil {
		ctx.Error(fmt.Sprintf("%s", err), fasthttp.StatusInternalServerError)
		return
	}

	forecast := string(body)
	ctx.SetContentType("application/json")
	json.NewEncoder(ctx).Encode(weatherReport{"Seattle", forecast})
}

func forecast(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("text/plain")
	ctx.Write([]byte("Cloudy"))
}

func main() {
	r := router.New()
	r.GET("/", weather)
	r.GET("/forecast", forecast)

	s := fasthttp.Server{
		TCPKeepalive:       true,
		TCPKeepalivePeriod: 30 * time.Second,
		Handler:            r.Handler,
	}
	fmt.Println("app listening on port 5000!")
	panic(s.ListenAndServe(":5000"))
}

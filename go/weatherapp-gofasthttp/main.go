package main

import (
	"fmt"
	"time"

	"github.com/fasthttp/router"
	jsoniter "github.com/json-iterator/go"
	"github.com/valyala/fasthttp"
	"os"
	"strings"
)

type weatherForecast struct {
	Weather string `json:"weather"`
}

type weatherReport struct {
	Location string `json:"location"`
	Weather  string `json:"weather"`
}

var json = jsoniter.ConfigFastest
var uri string

func weather(ctx *fasthttp.RequestCtx) {

	_, body, err := fasthttp.Get(nil, uri)
	if err != nil {
		ctx.Error(fmt.Sprintf("%s", err), fasthttp.StatusInternalServerError)
		return
	}

	var forecast weatherForecast
	json.Unmarshal(body, &forecast)

	ctx.SetContentType("application/json")
	json.NewEncoder(ctx).Encode(weatherReport{"Seattle", forecast.Weather})
}

func main() {
	uri = os.Getenv("FORECAST_SERVICE_URI")
	if uri == "" {
		uri = "http://localhost:8080"
	}
	uri = strings.TrimSuffix(uri, "/")
	uri += "/forecast"
	
	r := router.New()
	r.GET("/", weather)

	s := fasthttp.Server{
		TCPKeepalive:       true,
		TCPKeepalivePeriod: 30 * time.Second,
		Handler:            r.Handler,
	}
	fmt.Println("app listening on port 5000!")
	panic(s.ListenAndServe(":5000"))
}

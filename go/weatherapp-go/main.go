package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strings"
)

type weatherReport struct {
	Location string `json:"location"`
	Weather  string `json:"weather"`
}

var uri string

func weather(w http.ResponseWriter, r *http.Request) {
	resp, err := http.Get(uri)
	if err != nil {
		log.Fatalln(err)
		w.WriteHeader(500)
		return
	}

	defer resp.Body.Close()
	text, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Fatalln(err)
		w.WriteHeader(500)
		return
	}

	forecast := string(text)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(weatherReport{"Seattle", forecast})
}

func forecast(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/plain")
	w.Write([]byte("Cloudy"))
}

func main() {
	uri = os.Getenv("FORECAST_SERVICE_URI")
	if uri == "" {
		uri = "http://localhost:5002"
	}
	uri = strings.TrimSuffix(uri, "/")
	uri += "/forecast"

	http.HandleFunc("/", weather)
	http.HandleFunc("/forecast", forecast)
	fmt.Println("app listening on port 5000!")
	log.Fatal(http.ListenAndServe(":5000", nil))
}

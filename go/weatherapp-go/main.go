package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

type weatherReport struct {
	Location string `json:"location"`
	Weather  string `json:"weather"`
}

func weather(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	report := weatherReport{"Seattle", "Cloudy"}
	json.NewEncoder(w).Encode(report)
}

func main() {
	http.HandleFunc("/", weather)
	fmt.Println("app listening on port 5000!")
	log.Fatal(http.ListenAndServe(":5000", nil))
}

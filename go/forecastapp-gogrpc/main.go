package main

import (
	"context"
	"fmt"
	"log"
	"net"

	empty "github.com/golang/protobuf/ptypes/empty"

	weather "github.com/rynowak/benchmarks-playground/go/forecastapp-gogrpc/weather"
	"google.golang.org/grpc"
)

type weatherServer struct{}

func (s *weatherServer) GetForecast(ctx context.Context, request *empty.Empty) (*weather.ForecastResponse, error) {
	return &weather.ForecastResponse{Weather: "Cloudy"}, nil
}

func main() {
	lis, err := net.Listen("tcp", "localhost:8080")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	fmt.Println("app listening on port 8080!")

	var opts []grpc.ServerOption
	grpcServer := grpc.NewServer(opts...)

	var service weatherServer
	weather.RegisterForecastServiceServer(grpcServer, &service)
	grpcServer.Serve(lis)
}

#! /usr/local/bin/zsh

declare -A apps

apps[dotnet/weatherapp-controllers]="rynowak/weatherapp-controllers"
apps[dotnet/weatherapp-grpc]="rynowak/weatherapp-grpc"
apps[dotnet/weatherapp-route-to-code]="rynowak/weatherapp-route-to-code"
apps[go/forecastapp-gofasthttp]="rynowak/forecastapp-gofasthttp"
apps[go/forecastapp-gogrpc]="rynowak/forecastapp-gogrpc"
apps[go/weatherapp-go]="rynowak/weatherapp-go"
apps[go/weatherapp-gofasthttp]="rynowak/weatherapp-gofasthttp"
apps[java/weatherapp-springboot]="rynowak/weatherapp-springboot"
apps[java/weatherapp-springbootwebflux]="rynowak/weatherapp-springbootwebflux"
apps[java/weatherapp-quarkus]="rynowak/weatherapp-quarkus"
apps[node/weatherapp-express]="rynowak/weatherapp-express"
apps[node/weatherapp-expressclustered]="rynowak/weatherapp-expressclustered"
apps[python/weatherapp-flask]="rynowak/weatherapp-flask"
apps[rust/weatherapp-actix]="rynowak/weatherapp-actix"

for dir tag in ${(kv)apps}
do
    docker build "$dir" -t "$tag"
    if [ $? -ne 0 ]
    then
        echo "building $dir failed."
        exit 1
    fi
done

if [ "$1" = "--push" ]
then
    for dir tag in ${(kv)apps}
    do
        docker push "$tag"
        if [ $? -ne 0 ]
        then
            echo "pushing $tag failed."
            exit 1
        fi
    done
fi
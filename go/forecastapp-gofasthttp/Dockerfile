FROM golang:1.14.1-alpine3.11 AS build-env
RUN apk --no-cache add build-base git bzr mercurial gcc
WORKDIR /src
COPY . ./
RUN go build -o forecast -ldflags '-s'

FROM alpine 
WORKDIR /app
COPY --from=build-env /src/forecast /app/
EXPOSE 8080
ENTRYPOINT ./forecast
FROM golang:alpine AS build-env
RUN apk --no-cache add build-base git bzr mercurial gcc
WORKDIR /src
COPY . ./
RUN go build -o weather

FROM alpine 
WORKDIR /app
COPY --from=build-env /src/weather /app/
EXPOSE 5000
ENTRYPOINT ./weather
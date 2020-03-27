FROM rust:1 as builder
WORKDIR /app
COPY . .
RUN cargo build --release

FROM rust:1-slim-stretch
COPY --from=builder /app/target/release/weatherapp-actix .
EXPOSE 5000
ENTRYPOINT ["/weatherapp-actix"]
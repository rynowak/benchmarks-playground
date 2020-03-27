use actix_web::{web, client, ResponseError, App, HttpResponse, HttpServer, Result};
use serde::{Deserialize, Serialize};
use std::env;

struct AppState {
    forecast_uri: String,
    client: client::Client,
}

#[derive(Serialize, Deserialize)]
struct WeatherForecast {
    weather: String,
}

#[derive(Serialize, Deserialize)]
struct WeatherReport {
    location: String,
    forecast: String
}

async fn weather(data: web::Data<AppState>) -> Result<HttpResponse> {

    let request = data.get_ref()
            .client
            .get(&data.get_ref().forecast_uri)
            .send().await;
    
    let mut response = match request {
        Ok(r) => r,
        Err(e) => {
            return Ok(e.error_response());
        },
    };

    let forecast = match response.json::<WeatherForecast>().await {
        Ok(r) => r,
        Err(e) => {
            return Ok(e.error_response());
        }
    };

    return Ok(HttpResponse::Ok().json(WeatherReport{ 
        location: String::from("Seattle"),
        forecast: forecast.weather.clone(),
    }));
}

#[actix_rt::main]
async fn main() -> std::io::Result<()> {
    let mut uri = match env::var("FORECAST_SERVICE_URI") {
        Ok(var) => var,
        Err(_e) => String::from("http://localhost:8080"),
    };

    if &uri[uri.len()-1..] == "/" {
        uri.pop();
    }
    uri = uri + "/forecast";

    println!("Forecast service is: {}", uri);
    println!("Listening on: http://0.0.0.0:5000");

    HttpServer::new(move || {
        App::new()
            .data(AppState{ 
                forecast_uri: String::from(&uri),
                client: client::Client::default(),
            })
            .route("/", web::get().to(weather))
    })
    .backlog(1024)
    .bind("0.0.0.0:5000")?
    .run()
    .await
}
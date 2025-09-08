# WeatherAPI.com Code Challenge
I used [WeatherAPI.com public API](https://www.weatherapi.com/) for retrieving a forecast for next day for some european cities.
## Project structure
- *src/main/java/org/tymosh/model* - a POJO for working with JSON data in Java application
- *src/main/java/org/tymosh/nethttp* - a variant of solution this task using Java's built-in **HttpClient** and **Gson** library for data deserialization
- *src/main/java/org/tymosh/retrofit* - a variant of solution this task using **Retrofit** library for API interaction
## Configuration
This project uses the **dotenv-java** library for reading API key from `.env` file that you must create in the root of the project. `env.example` is a template for your file.
## Building and running
Use next command for build and run Gradle project:
`./gradlew run`
## Example of output
          Data Point             MinTemp             MaxTemp            Humidity          Wind Speed      Wind Direction
            Chisinau               12,70               29,30               39,00                8,58                 ENE
              Madrid               18,90               27,70               47,00                9,40                 WSW
                Kyiv               14,90               26,00               61,00               11,35                  NE
           Amsterdam               14,60               22,10               72,00               11,00                   W
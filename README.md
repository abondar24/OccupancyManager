# Occupancy Manager

Hotel room occupancy manager.

## API
```yaml
http://localhost:8080/occupancy?premium={premiumRooms}&economy={economyRooms} - fetch occupancy usage calculations

Response:
  200 - occupancy usage
  {
    "premium": {
      "rooms": 6,
      "price": 1054.0
    },
    "economy": {
      "rooms": 4,
      "price": 189.99
    }
  }
  400 - Invalid path params
  502 - Error reading guests json

```

## Build and Run

```yaml
./gradlew clean build 

java -jar <jar-folder>/OccupancyManager-1.0-SNAPSHOT.jar
```
Or 
```yaml
./gradlew bootRun
```
Run tests

```yaml
./gradlew clean test
```

# Garmin FIT Parser

A Java application for parsing and analyzing Garmin `.FIT` activity files.

## Features

- Parse Garmin `.FIT` files using the Garmin FIT Java SDK
- Convert FIT messages into application-specific domain objects
- Support different activity types, including:
    - Walking
    - Running
    - Hiking
    - Rucking
    - Meditation
    - Floor climbing
    - Yoga
- Extract activity data such as:
    - Start time and duration
    - Sport and sub-sport
    - Elevation data
    - Device information
    - Heart-rate zones
- Configurable activity analyzers, for example:
    - Walking time below a maximum pace
    - Meditation days
    - Walking time in selected heart-rate zones
- Configure analyzers and FIT parsing through `application.yml`
- Optional automatic FIT file renaming based on activity data
- Logging through Log4j2
- Unit tests using JUnit

## Technology

- Java
- Spring Boot
- Gradle
- Garmin FIT Java SDK
- Log4j2
- JUnit

## Configuration

Main configuration is located in:

```text
src/main/resources/application.yml
```

It defines:

- FIT input directory
- FIT parser options
- Message debugging/printing
- File renaming
- Activity analyzer rules
- Logging configuration

## Running

Set the FIT input directory in `application.yml` and run:

```bash
./gradlew bootRun
```

On Windows:

```bat
gradlew.bat bootRun
```

## Project Structure

```text
src/main/java/malecluk/garminparser/
├── fit/           # FIT file parsing
├── model/         # Domain model
├── processing/    # Activity analyzers
├── configuration/ # Application configuration
└── utils/         # Utility classes
```

The project is primarily intended as a personal Garmin activity data parser and analysis tool.
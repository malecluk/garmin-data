# Garmin FIT Parser

Java application for parsing Garmin `.fit` activity files, mapping FIT messages to a domain model, and running configurable activity analyzers.

## Features

* Scans a configured directory for Garmin `.fit` files.
* Parses FIT files using the Garmin FIT SDK.
* Validates parsed FIT messages before mapping.
* Maps FIT data to domain `Activity` objects.
* Supports activity-specific models, including:

  * Walking
  * Running
  * Hiking
  * Rucking
  * Meditation
  * Floor Climbing
  * Training
  * Yoga
* Falls back to a generic `Activity` for unsupported activity types.
* Maps session-level heart-rate zone data.
* Runs multiple configurable activity analyzers.
* Supports multiple independent configurations of the same analyzer.
* Optionally renames FIT files based on parsed activity data.
* Provides console output for parsed activities and processing results.

## Processing pipeline

```text
FIT files
    │
    ▼
FitFileScanner
    │
    ▼
FitFileParser
    │
    ▼
ParsedFitFileMessagesDTO
    │
    ▼
ParsedMessagesDTOValidator
    │
    ▼
FitActivityMapper
    │
    ▼
Activity
    │
    ▼
ActivityProcessor
    │
    ├── ActivityAnalyzer
    ├── ActivityAnalyzer
    └── ActivityAnalyzer
    │
    ▼
ActivityProcessingResult
```

The application separates FIT parsing from the domain model and from activity processing. This allows multiple analyzers to consume the same parsed activities independently.

## Project structure

```text
src/main/java/malecluk/garminparser/
├── fileparser/    # FIT file scanning, parsing and message handling
├── mappers/       # FIT SDK → domain model mapping
├── model/         # Domain activity and value objects
├── processing/    # Activity analyzers and processing results
└── utils/         # Console printers and utility classes
```

## Activity processing

Activity analyzers are configured in `application.yml`.

Each configuration entry creates an independent analyzer instance. This makes it possible to run several analyses of the same type with different date ranges or thresholds.

### Average walking pace

Counts the moving time of walking activities whose average pace is below the configured limit.

```yaml
activity-processing:
  walking-avg-pace-time:
    - name: "September fast walking"
      start-date: 2026-09-01T00:00:01
      end-date: 2026-09-30T23:59:59
      required-duration: PT3H
      max-average-pace: 10.5
```

The analyzer uses `totalTimerTime`, not `totalElapsedTime`, so paused time is not included.

`max-average-pace` is expressed in minutes per kilometre. The configured limit is exclusive.

### Meditation days

Counts distinct calendar days containing meditation activities.

```yaml
activity-processing:
  meditation-days:
    - name: "September meditation"
      start-date: 2026-09-01T00:00:01
      end-date: 2026-09-30T23:59:59
      required-days: 7
```

Multiple meditation activities on the same day count as one day.

### Walking in heart-rate zones

Counts walking time spent in selected heart-rate zones.

```yaml
activity-processing:
  walking-hr-zones:
    - name: "September heart-rate walking"
      start-date: 2026-09-01T00:00:01
      end-date: 2026-09-30T23:59:59
      required-duration: PT3H
      required-zones: [2, 3]
```

Zone numbers refer to the actual `HeartRateZone.number()` value, not to the position of the zone in the list.

For example, if an activity contains zones:

```text
[0, 1, 4, 5]
```

requesting zone `3` does not select the fourth element. Zone `3` is simply not present.

## Heart-rate zones

Session-level FIT `TimeInZoneMesg` data is mapped to `HeartRateZone` objects.

A zone contains:

* zone number
* minimum heart rate
* maximum heart rate
* time spent in the zone

Open-ended boundaries are represented by `null`.

For example:

```text
zone 0: <89 bpm
zone 1: 89–106 bpm
zone 2: 107–124 bpm
zone 3: 125–141 bpm
zone 4: 142–159 bpm
zone 5: 160–177 bpm
zone 6: >=178 bpm
```

## FIT file renaming

FIT files can optionally be renamed using information extracted from the activity.

Enable it with:

```yaml
fit:
  file-renamer:
    enabled: true
```

Example:

```text
24375337180_ACTIVITY.fit
        ↓
24375337180_2026-09-15_21-12-51_FLOOR_CLIMBING-GENERIC.fit
```

The renamer:

* works with Garmin's numeric FIT filenames,
* accepts `.fit` case-insensitively,
* preserves the original Garmin file ID,
* adds the activity timestamp and activity type,
* does not overwrite an existing target file.

## Configuration

The main configuration is located in:

```text
src/main/resources/application.yml
```

Important sections include:

```yaml
fit:
  files-scanner:
    directory-path: "..."

  parser:
    strict: true
    unknown-fields: true

  file-renamer:
    enabled: false
```

and:

```yaml
activity-processing:
  ...
```

The configured scanner directory should point to the directory containing the FIT files to process.

## FIT message parsing

The parser uses the Garmin FIT SDK and collects supported FIT messages into `ParsedFitFileMessagesDTO`.

The application validates the parsed data before creating domain activities.

The parser can be configured to:

* use strict FIT parsing,
* accept unknown FIT fields,
* print selected FIT message types for debugging.

Debug output is controlled through the `fit.mesg-dto-printer` section of `application.yml`.

## Requirements

* Java 21
* Gradle
* Garmin FIT SDK

The project is currently built with:

* Spring Boot
* Garmin FIT SDK
* Log4j2
* JUnit

## Running

Configure the FIT input directory in:

```text
src/main/resources/application.yml
```

Then run the application with Gradle:

```bash
./gradlew bootRun
```

On Windows:

```bat
gradlew.bat bootRun
```

## Testing

Run the test suite with:

```bash
./gradlew test
```

The tests cover FIT parsing, mapping, file scanning/renaming, activity processing, and individual analyzers.

## License

This project uses the Garmin FIT SDK. See the Garmin FIT SDK license and the project dependencies for their respective licensing terms.

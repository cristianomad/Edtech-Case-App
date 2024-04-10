# EdTech Case Android Application

This is an Android app prototype using the Kotlin programming language that caters to the needs of learners seeking to access video lessons online.

The key features of the app includes:

1. **Video Lesson Access**: Users can browse and access a library of video lessons hosted online. The app will provide a user-friendly interface for easy navigation through available lessons.

2. **Limited Offline Capacity**: To cater to learners in areas with unreliable internet connectivity, the app offers the ability to download a limited number of lessons for offline viewing. This offline capacity allow users to continue learning even without an active internet connection.

3. **Note-taking Feature**: Learners can make notes at specific timestamps within the video lessons. This feature facilitates active learning and help users jot down important information or questions for later reference.

4. **Daily Log-in Reward Feature**: To incentivize and engage users, the app incorporates a daily log-in reward system. Users who log in daily receive a congratulatory message acknowledging their efforts.

5. **User-Friendly Design**: The app will be designed with a focus on user-friendliness, ensuring that even those with limited technical expertise can navigate it easily.

| | |
| --- | --- |
| ![chapters](https://ik.imagekit.io/lhpsfdvnd/Screenshot_2023-10-04-00-13-16-18_e9003ef0eea51102663529086d1a8477.jpg?updatedAt=1696379387543) | ![lesson](https://ik.imagekit.io/lhpsfdvnd/Screenshot_2023-10-04-00-13-57-18_e9003ef0eea51102663529086d1a8477.jpg?updatedAt=1696379293583) |

## Techinical documentation

[Click here to see the complete technical documentation](https://cristiano-madeira.notion.site/EdTech-Application-Prototype-c50ebe115ee54172b3a62aa8ff3639fa?pvs=74)

## Setup

- Download the latest stable [**Android Studio**](https://developer.android.com/studio)
- Import the project
- Run the app

## Unit Tests

To execute the unit tests report run:
```bash
./gradlew test
```
## Unit Test Coverage

To generate the test coverage report run:
```bash
./gradlew koverHtmlReportDebug
```
The report will be generated in `app/build/reports/kover/htmlDebug/index.html`

## Instrumented Tests

To execute the instrumented tests report run:
```bash
./gradlew connectedAndroidTest
```
I recommend using the emulator.

The report will be generated in `app/build/reports/androidTests/connected/debug/index.html`

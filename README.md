# Download Website with Selenium

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-3.11.0-43B02A.svg)](https://www.selenium.dev/)
[![Maven](https://img.shields.io/badge/Maven-Wrapper-C71A36.svg)](https://maven.apache.org/)

A Java and Selenium WebDriver website downloader that recursively crawls web pages in Firefox, mirrors HTML pages and static assets (CSS, JavaScript, images) to a local directory structure, and includes a log-based recovery utility for fetching missing 404 assets.

## Key Features

- **Recursive Selenium Crawling**: Launches Firefox via GeckoDriver (`MyTest`), starts from a configured URL, discovers internal `<a href>` links under `info.basehost`, and tracks visited URLs to avoid duplicate requests.
- **Static Asset Mirroring**: Extracts and downloads resources referenced in `<link href>`, `<script src>`, and `<img src>` elements while preserving the site's relative folder hierarchy locally.
- **Duplicate & Overwrite Control**: Strips URL query strings (`?`) and fragments (`#`) when normalizing links and skips already-downloaded files unless `info.overridefile=true` is set.
- **HTTP & HTTPS Download Support**: Downloads page content and binary assets over HTTP and HTTPS via `ContentUtils`.
- **Missing-Asset Recovery (`ValidData`)**: Parses 404/error logs from `error.txt` using the regex in `regex.txt`, rewrites local host URLs (`host.tofind`) to the remote origin (`host.toreplace`), and downloads the missing files directly into the local mirror.

## Tech Stack

- **Language**: Java 8
- **Browser Automation**: Selenium WebDriver 3.11.0 + Firefox GeckoDriver (`drivers/geckodriver.exe`, `drivers/geckodriver`)
- **Build & Test Runner**: Apache Maven (with `mvnw` / `mvnw.cmd` wrapper), JUnit 4, Maven Surefire Plugin
- **Utilities & Logging**: Apache Commons (`commons-lang3`, `commons-io`), SLF4J / Log4j 2

## Project Structure

```text
download-web-site/
├── config.properties                    # Target site URL, local output path, and host rewrite settings
├── pom.xml                              # Maven dependencies and Java 8 / Surefire build configuration
├── mvnw / mvnw.cmd                      # Maven Wrapper scripts (Unix / Windows)
├── regex.txt                            # URL extraction regex used by ValidData
├── error.txt                            # Input log file containing missing/404 asset URLs for recovery
├── drivers/
│   ├── geckodriver                      # Firefox GeckoDriver binary (Linux/Unix)
│   └── geckodriver.exe                  # Firefox GeckoDriver binary (Windows)
└── src/
    ├── main/java/com/sacle/
    │   ├── ValidData.java               # Utility to extract and re-download missing assets from error.txt
    │   ├── common/Constants.java        # Property keys and asset regex constants
    │   ├── model/FileInfo.java          # Maps remote URLs to local relative paths and filenames
    │   ├── service/                     # Configuration loader (Config / ConfigImpl)
    │   └── utils/                       # HTTP/HTTPS download & file-saving helpers (ContentUtils, FileUtils)
    └── test/java/com/sacletest/
        ├── MyTest.java                  # JUnit + Selenium Firefox crawler that downloads HTML & assets
        ├── common/Constants.java
        ├── model/FileInfo.java
        ├── service/
        └── utils/
```

## Prerequisites

- **JDK 8** (or compatible JDK with Java 8 compilation support)
- **Mozilla Firefox** installed locally (used by Selenium `FirefoxDriver`)
- **Maven** (optional — the repository includes the Maven Wrapper `mvnw` / `mvnw.cmd`)

> **Platform note:** `src/test/java/com/sacletest/MyTest.java` loads `drivers/geckodriver.exe` by default (Windows). On Linux/macOS, update the driver path or use a compatible GeckoDriver binary for your installed Firefox version.

## Configuration

Edit `config.properties` in the project root before running:

| Property | Description | Example |
| --- | --- | --- |
| `info.basehost` | Base host prefix used to filter in-scope links/assets and compute relative folder paths | `https://preview.keenthemes.com/keen/` |
| `info.sitetodownload` | Starting page URL to crawl | `https://preview.keenthemes.com/keen/` |
| `info.rootfolder` | Local directory where downloaded HTML and assets are saved (created automatically) | `C:\\xampp\\htdocs\\html\\keen\\` |
| `info.sitename` | Site label printed in console output | `keen` |
| `info.overridefile` | Overwrite existing files if `true`; skip existing files if `false` | `false` |
| `host.tofind` | Local host URL prefix to match in `error.txt` (used by `ValidData`) | `http://localhost/html/keen/` |
| `host.toreplace` | Remote host URL prefix to substitute when fetching missing assets (used by `ValidData`) | `https://preview.keenthemes.com/keen/` |

## Usage

### 1. Crawl and Download a Website

Run the Maven build to execute the Selenium crawler test (`com.sacletest.MyTest`):

```bash
# Linux / macOS
./mvnw clean install

# Windows
mvnw.cmd clean install
```

You can also run only the test phase:

```bash
./mvnw test -Dtest=com.sacletest.MyTest
```

### 2. Recover Missing (404) Assets from Logs

If browsing the downloaded mirror locally reveals missing resources (such as fonts or background images referenced from CSS):

1. Paste the browser console or server 404 log lines into `error.txt`.
2. Ensure `host.tofind` and `host.toreplace` in `config.properties` map your local mirror URL back to the original remote host.
3. Compile and run `com.sacle.ValidData` from your IDE, or via the command line with Maven's dependencies on the classpath:

```bash
./mvnw compile dependency:copy-dependencies
java -cp "target/classes:target/dependency/*" com.sacle.ValidData
```

*(On Windows, use `;` instead of `:` as the classpath separator.)*

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

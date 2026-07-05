# bip-client

> **Language**: English · [Русский](README.ru.md)

JavaFX desktop client for the **bipApi** teaching-load server. Calls the server's REST API
(`/bipapi/...`) and imports/exports XLSX via Apache POI.

Server repo: https://github.com/Enrinko/bipApi

## Build & run

### Prerequisites

- **JDK 17 or newer** (tested on JDK 25 and Corretto 18). The code is compiled to Java 17
  bytecode, so it runs on any launcher JDK ≥ 17.
- **No global Maven needed** — the Maven Wrapper (`mvnw` / `mvnw.cmd`) is bundled and pins
  Maven 3.9.9.
- The **bipApi server must be running first** (default `http://localhost:28242`). See the
  server repo's Quick start (`docker compose up --wait`).

### Run

```bash
# macOS / Linux / Git Bash
./mvnw javafx:run
```

```powershell
# Windows PowerShell / cmd
.\mvnw.cmd javafx:run
```

From **IntelliJ IDEA**: open this `pom.xml` as a project, then run the Maven goal
`Plugins → javafx → javafx:run`. Make sure the Maven Runner JRE is **17+**
(*Settings → Build Tools → Maven → Runner → JRE*) — an older JDK will fail to launch the
Java 17 bytecode.

### Configuration

Runtime settings live in `src/main/resources/config.properties` and can be overridden
without editing the file. Precedence: **system property → environment variable → file → default**.

| Setting | System property | Environment variable | Default |
| --- | --- | --- | --- |
| Server base URL | `bip.api.base-url` | `BIP_API_BASE_URL` | `http://localhost:28242/bipapi/` |
| Default XLSX path | `bip.xlsx.default-path` | `BIP_XLSX_DEFAULT_PATH` | *(empty)* |

```bash
# Point the client at a different server for one run:
./mvnw javafx:run -Dbip.api.base-url=http://192.168.1.50:28242/bipapi/
```

## Packaging a Windows .exe

Produces a self-contained app-image with a bundled Java runtime — the target machine needs **no
JDK installed**. Requires a **JDK 21+** (it ships `jpackage`).

```bash
./mvnw -B -DskipTests clean package \
  dependency:copy-dependencies -DoutputDirectory=target/deps -DincludeScope=runtime
mkdir -p target/jpkg-input && cp target/deps/*.jar target/jpkg-input/ && cp target/*.jar target/jpkg-input/

jpackage --type app-image --name bip-client \
  --input target/jpkg-input \
  --main-jar "$(basename "$(ls target/*.jar | head -1)")" \
  --main-class client.bip.cleintdontdeletefuck.Launcher \
  --dest target/dist

# → target/dist/bip-client/bip-client.exe
```

The app is packaged through the non-JavaFX `Launcher` class (see `Launcher.java`): a main class
that extends `Application` would otherwise fail with *"JavaFX runtime components are missing"* when
launched from the classpath.

CI does this automatically on Windows — `.github/workflows/build-exe.yml` runs on every `v*` tag and
on manual dispatch, and uploads the zipped app-image as a build artifact (attaching it to the
GitHub Release for tag builds).

## Security

See [SECURITY.md](SECURITY.md) ([Русский](SECURITY.ru.md)) for the security posture, dependency
policy, and how to report a vulnerability.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for branch naming, commit format, and the PR checklist.

## License

Released under the [MIT License](LICENSE).

# Security

> **Language**: English · [Русский](SECURITY.ru.md)

Security posture of **bip-client** — a JavaFX desktop app that talks to the [bipApi](https://github.com/Enrinko/bipApi)
server over REST and reads/writes XLSX via Apache POI. Delivery target is a **standalone Windows
`.exe`** (jpackage app-image), so container/Kubernetes baseline items are marked N/A and replaced
with desktop- and supply-chain-level controls.

## Reporting a vulnerability

Do **not** open a public issue for security problems. Email **enrinkopiece@gmail.com** with a
description, reproduction steps, and affected commit. Please allow a reasonable fix window before
public disclosure.

## Threat model (in scope)

- Malicious or backdoored release binary (supply-chain / tampered `.exe`).
- Vulnerable third-party dependency (POI, OkHttp, and their transitives).
- Interception/tampering of client↔server traffic (cleartext HTTP).
- Leak of local teaching-load data (XLSX) via source control.

Out of scope: compromise of the user's OS, physical machine access, attacks on the server itself
(covered by the server repo's `SECURITY.md`).

## Enforced baseline

| # | Control | Status | Where |
| --- | --- | --- | --- |
| 1 | Dependency versions pinned (no ranges / `LATEST`) | ✅ | `pom.xml` |
| 2 | No known-EOL / stale libraries | ✅ | `pom.xml` (POI 5.4.1, OkHttp 4.12.0, Gson 2.11.0, JavaFX 21.0.5) |
| 3 | TLS certificate validation on by default | ✅ | OkHttp default trust manager (`HTTPUtils`) |
| 4 | No secrets / credentials in source | ✅ | client stores none; server URL is config, not a secret |
| 5 | Teaching-load data kept out of VCS | ⚠️ | `.gitignore` (`*.xlsx`); legacy tracked files still need untracking — see Overrides |
| 6 | Reproducible build (pinned Maven Wrapper) | ✅ | `.mvn/wrapper/maven-wrapper.properties` |
| 7 | CI secret scan (gitleaks) | ✅ | `.github/workflows/ci.yml` |
| 8 | CI dependency vuln scan (Trivy fs) | ⚠️ report-only | `.github/workflows/ci.yml` |
| 9 | Packaged runtime is self-contained (bundled JRE, no system Java) | ✅ | jpackage app-image (`build-exe.yml`) |
| 10 | Release binary code-signed (Authenticode) | ❌ | not implemented — see Recommended |
| 11 | Container / K8s hardening | N/A | desktop app, no container target |

## Overrides (declared by the project)

| Item | Override | Reason | Recommended fix |
| --- | --- | --- | --- |
| Server base URL | Default is cleartext `http://localhost:28242/bipapi/` | Local-dev default; client and server run on the same machine. | Set `BIP_API_BASE_URL` (or `-Dbip.api.base-url`) to an **`https://`** URL whenever the server is remote — the app already honors the override. |
| Trivy (baseline #8) | `exit-code: 0` (report-only) | Fresh pipeline; avoid a permanently red build before a baseline scan exists. | Review the first scan, then set `exit-code: 1` to gate. |
| Legacy `*.xlsx` in history | Spreadsheets were committed before the ignore rule | They may contain real teaching-load data (teacher names, groups). | `git rm --cached *.xlsx`; if sensitive, purge history with `git filter-repo`. |
| GitHub Actions pinning | Pinned to version tags, not commit SHA | Readability; mirrors the server repo. | Pin to commit SHA once Dependabot is enabled. |

## Recommended but not implemented

- [x] Upgraded OkHttp 4.2.2 → **4.12.0** (fixes CVE-2021-0341) and POI 5.2.0 → **5.4.1** (pulls patched `commons-compress`, `commons-io`, `xmlbeans`)
- [ ] Authenticode-sign the `.exe` (signtool + code-signing cert) so Windows SmartScreen trusts the download
- [ ] HTTPS + certificate pinning when the server is not on localhost
- [ ] Remove tracked spreadsheets from git history
- [ ] Enable Dependabot for this repo
- [ ] Modular (jlink) build to drop the "unnamed module" classpath warning

## Secrets & data handling

| Layer | Mechanism |
| --- | --- |
| Server URL | `config.properties` / `BIP_API_BASE_URL` env / `-Dbip.api.base-url` — **not a secret** |
| Credentials | none — the client currently sends no auth |
| Local spreadsheets | stay on the user's disk; `*.xlsx` is gitignored |

If the server later requires authentication, store tokens in the OS credential store (Windows
Credential Manager / Keychain) — never in `config.properties` or source.

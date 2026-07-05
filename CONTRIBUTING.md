# Contributing to bip-client

## Local setup

```bash
# Requires JDK 17+ (tested on JDK 25). No global Maven needed — the wrapper is bundled.
./mvnw javafx:run            # macOS / Linux / Git Bash
.\mvnw.cmd javafx:run        # Windows PowerShell / cmd
```

The **bipApi server must be running first** (default `http://localhost:28242`). See the
[server repo](https://github.com/Enrinko/bipApi) Quick start.

Build & package (no run):

```bash
./mvnw -B -ntp -DskipTests package
```

Packaging a standalone Windows `.exe` is documented in the [README](README.md#packaging-a-windows-exe)
and automated in `.github/workflows/build-exe.yml`.

## Branch naming

`<type>/<short-slug>` where `<type>` ∈ `feat` `fix` `refactor` `perf` `docs` `chore` `test`
`security`. Example: `feat/xlsx-export-dialog`, `fix/http-error-handling`.

## Commit messages

[Conventional Commits](https://www.conventionalcommits.org/):
`<type>(<scope>): <imperative summary ≤72 chars>`, optional body explaining **why**.

## Before opening a PR

- [ ] `./mvnw -B package` succeeds
- [ ] No secrets committed (the CI `gitleaks` job will block them anyway)
- [ ] No spreadsheet / teaching-load data added (`*.xlsx` is gitignored — keep it that way)
- [ ] Dependency versions stay pinned (no version ranges, no `LATEST`)
- [ ] `SECURITY.md` updated if the security posture changed

## Security

Report vulnerabilities privately per [SECURITY.md](SECURITY.md) — never as a public issue.
The client stores no credentials; keep it that way (the server base URL is config, not a secret).

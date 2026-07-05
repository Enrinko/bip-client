# bip-client

> **Язык**: Русский · [English](README.md)

JavaFX-клиент для сервера учебной нагрузки **bipApi**. Обращается к REST API сервера
(`/bipapi/...`) и импортирует/экспортирует XLSX через Apache POI.

Репозиторий сервера: https://github.com/Enrinko/bipApi

## Сборка и запуск

### Требования

- **JDK 17 или новее** (проверено на JDK 25 и Corretto 18). Код компилируется в байткод Java 17,
  поэтому запускается на любом JDK ≥ 17.
- **Глобальный Maven не нужен** — Maven Wrapper (`mvnw` / `mvnw.cmd`) вложён и пинит Maven 3.9.9.
- Сначала должен быть запущен **сервер bipApi** (по умолчанию `http://localhost:28242`). См.
  Quick start в репозитории сервера (`docker compose up --wait`).

### Запуск

```bash
# macOS / Linux / Git Bash
./mvnw javafx:run
```

```powershell
# Windows PowerShell / cmd
.\mvnw.cmd javafx:run
```

Из **IntelliJ IDEA**: откройте `pom.xml` как проект, затем запустите Maven-цель
`Plugins → javafx → javafx:run`. Убедитесь, что JRE Maven Runner — **17+**
(*Settings → Build Tools → Maven → Runner → JRE*).

### Конфигурация

Настройки живут в `src/main/resources/config.properties` и переопределяются без правки файла.
Приоритет: **системное свойство → переменная окружения → файл → значение по умолчанию**.

| Настройка | Системное свойство | Переменная окружения | По умолчанию |
| --- | --- | --- | --- |
| Base URL сервера | `bip.api.base-url` | `BIP_API_BASE_URL` | `http://localhost:28242/bipapi/` |
| Путь к XLSX по умолчанию | `bip.xlsx.default-path` | `BIP_XLSX_DEFAULT_PATH` | *(пусто)* |

```bash
# Указать другой сервер на один запуск:
./mvnw javafx:run -Dbip.api.base-url=http://192.168.1.50:28242/bipapi/
```

## Сборка Windows `.exe`

Создаёт самодостаточный app-image со встроенным Java-рантаймом — на целевой машине **JDK не нужен**.
Требуется **JDK 21+** (в нём есть `jpackage`).

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

Пакетирование идёт через не-JavaFX класс `Launcher` (см. `Launcher.java`): главный класс,
наследующий `Application`, иначе падает с *«JavaFX runtime components are missing»* при запуске
с classpath.

CI делает это автоматически на Windows — `.github/workflows/build-exe.yml` запускается на каждый
тег `v*` и по ручному запуску (`workflow_dispatch`), загружает zip с app-image как артефакт сборки
(для тегов — прикрепляет к GitHub Release).

## Безопасность

См. [SECURITY.ru.md](SECURITY.ru.md) ([English](SECURITY.md)) — контракт безопасности, политика
зависимостей и как сообщить об уязвимости.

## Контрибьюшен

См. [CONTRIBUTING.md](CONTRIBUTING.md) — правила веток, формат коммитов, чек-лист PR.

## Лицензия

Распространяется под лицензией [MIT](LICENSE).

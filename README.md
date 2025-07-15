# ERP Inventory Backend

## Описание

ERP-модуль складского учёта для малого бизнеса на Spring Boot + MongoDB.

- Аутентификация: JWT (email + password)
- Роли: ADMIN, STAFF
- Изоляция данных по companyId
- Swagger UI: автогенерация документации

## Запуск

1. Установить зависимости:
   ```
   ./gradlew build
   ```
2. Запустить MongoDB (локально, порт 27017)
3. Запустить приложение:
   ```
   ./gradlew bootRun
   ```
4. Открыть Swagger UI:
   - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Авторизация

- POST `/api/auth/login` — получить JWT (email, password)
- Использовать JWT в заголовке:
  ```
  Authorization: Bearer <token>
  ```

## Основные эндпоинты

- `/api/companies` — компании (только ADMIN)
- `/api/users` — пользователи (только своей компании, CRUD — только ADMIN)
- `/api/warehouses` — склады (только своей компании, CRUD — только ADMIN)
- `/api/products` — продукты (только своей компании, CRUD — только ADMIN)
- `/api/stock-change-logs` — логи изменений остатков (только своей компании, удаление — только ADMIN)
- `/api/whatsapp` — парсинг текстовых команд для изменения остатков

## Структура проекта

- `entity` — сущности MongoDB
- `dto` — DTO для запросов/ответов
- `controller` — REST-контроллеры
- `service` — бизнес-логика
- `repository` — репозитории MongoDB
- `mapper` — преобразование entity <-> DTO
- `security` — JWT, фильтры, контекст
- `exception` — глобальная обработка ошибок
- `config` — конфигурация Spring/Swagger

## Документация API

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### Пагинация и фильтрация

- Для списков поддерживаются параметры `page`, `size`, фильтры по полям (см. Swagger UI)

### Тесты

- Unit и интеграционные тесты: JUnit, Mockito (см. src/test/java) 
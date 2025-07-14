# ERP Складской Учёт

Backend-модуль ERP складского учёта для малого бизнеса на Spring Boot с MongoDB.

## Технологии

- **Spring Boot 3.5.3** - основной фреймворк
- **MongoDB** - база данных
- **Spring Security** - аутентификация и авторизация
- **Spring Data MongoDB** - работа с базой данных
- **Lombok** - уменьшение boilerplate кода
- **Docker & Docker Compose** - контейнеризация

## Структура проекта

```
src/main/java/com/erp/inventory/
├── entity/          # Сущности MongoDB
├── repository/      # Репозитории Spring Data
├── service/         # Бизнес-логика
├── controller/      # REST контроллеры
├── dto/            # Data Transfer Objects
├── security/       # Конфигурация безопасности
└── config/         # Конфигурации приложения
```

## Сущности

### Company
- `id` - уникальный идентификатор
- `name` - название компании
- `settings` - настройки компании (Map)
- `createdAt` - дата создания

### User
- `id` - уникальный идентификатор
- `email` - email пользователя
- `password` - хешированный пароль (BCrypt)
- `fullName` - полное имя
- `role` - роль (ADMIN, MANAGER, OPERATOR)
- `companyId` - ID компании
- `active` - активность аккаунта
- `createdAt` - дата создания

### Warehouse
- `id` - уникальный идентификатор
- `address` - адрес склада
- `companyId` - ID компании

### Product
- `id` - уникальный идентификатор
- `companyId` - ID компании
- `warehouseId` - ID склада
- `data` - данные продукта (Map)
- `createdAt` - дата создания
- `updatedAt` - дата обновления

### StockChangeLog
- `id` - уникальный идентификатор
- `productId` - ID продукта
- `userId` - ID пользователя
- `warehouseId` - ID склада
- `changeAmount` - количество изменения
- `comment` - комментарий
- `timestamp` - время изменения

## API Endpoints

### Аутентификация
- `POST /api/auth/login` - вход в систему

### Компании
- `GET /api/companies` - список всех компаний
- `GET /api/companies/{id}` - получить компанию по ID
- `POST /api/companies` - создать компанию
- `PUT /api/companies/{id}` - обновить компанию
- `DELETE /api/companies/{id}` - удалить компанию

### Пользователи
- `GET /api/users/company/{companyId}` - пользователи компании
- `GET /api/users/{id}` - получить пользователя по ID
- `POST /api/users` - создать пользователя
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя
- `PATCH /api/users/{id}/toggle-status` - изменить статус пользователя

### Склады
- `GET /api/warehouses/company/{companyId}` - склады компании
- `GET /api/warehouses/{id}` - получить склад по ID
- `POST /api/warehouses` - создать склад
- `PUT /api/warehouses/{id}` - обновить склад
- `DELETE /api/warehouses/{id}` - удалить склад

### Продукты
- `GET /api/products/company/{companyId}` - продукты компании
- `GET /api/products/company/{companyId}/warehouse/{warehouseId}` - продукты склада
- `GET /api/products/{id}` - получить продукт по ID
- `POST /api/products` - создать продукт
- `PUT /api/products/{id}` - обновить продукт
- `DELETE /api/products/{id}` - удалить продукт
- `PATCH /api/products/{id}/stock` - изменить остаток

### Логи изменений
- `GET /api/stock-logs/product/{productId}` - логи продукта
- `GET /api/stock-logs/warehouse/{warehouseId}` - логи склада
- `GET /api/stock-logs/{id}` - получить лог по ID

### WhatsApp интеграция
- `POST /api/whatsapp` - обработка WhatsApp сообщений

## WhatsApp интеграция

Система поддерживает обработку сообщений вида:
- `"Краска Белая -1"` - уменьшить остаток на 1
- `"Краска Белая +5"` - увеличить остаток на 5

### Пример запроса:
```json
{
  "message": "Краска Белая -1",
  "companyId": "company123"
}
```

## Запуск

### Локально

1. Установите MongoDB локально или используйте Docker
2. Настройте `application.yml` для подключения к MongoDB
3. Запустите приложение:
```bash
./gradlew bootRun
```

### С Docker Compose

1. Запустите все сервисы:
```bash
docker-compose up -d
```

2. Приложение будет доступно по адресу: `http://localhost:8080`

### Сборка JAR

```bash
./gradlew build
java -jar build/libs/erp-inventory-0.0.1-SNAPSHOT.jar
```

## Мультикомпанийность

Все операции привязаны к `companyId`. Пользователи могут работать только с данными своей компании.

## Безопасность

- Пароли хешируются с помощью BCrypt
- Spring Security для аутентификации
- Роли пользователей: ADMIN, MANAGER, OPERATOR
- Валидация входных данных с помощью Bean Validation

## Мониторинг

- Health check: `GET /api/actuator/health`
- Metrics: `GET /api/actuator/metrics`
- Info: `GET /api/actuator/info` 
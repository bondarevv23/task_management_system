# task_management_system

В проекте используется Keycloak, он настроен в compose.yaml, 
при запуске контейнера конфигурация realm'а экспортируется из файла
[keycloak-config/import/tasks-manager-realm.json](keycloak-config/import/tasks-manager-realm.json)
вместе со всеми пользователями.

Для тестирования проекта в realm добавлено 5 пользователей, они
имеют реквизиты вида: 
```json
{
  "email": "${userRole}${userNumber}@test.com",
  "password": "password"
}
```

Для работы с базами данных используются PostgreSQL и liquibase
[миграции](src/main/resources/db/changelog/db.changelog-master.yaml), все
миграции написаны на SQL.

Swagger документация и swagger-ui доступны по адресам http://localhost:8081/api-docs и
http://localhost:8081/swagger-ui/index.html соответственно.

Для фильтрации rest запросов используется библиотека [Spring Filter](https://github.com/turkraft/springfilter).

Единственный "открытый" контроллер **/api/v1/users/..**, который используется для аутентификации,
ограничен по количеству запросов при помощи **bucket4j**. Для работы bucket4j используется
**caffeine cache**.

Запрос и ответ каждого эндпоинта [логируется](src/main/java/com/github/bondarevv23/task_management_system/aop/InboundRequestAdvice.java),
для этого используется **spring-aop**.

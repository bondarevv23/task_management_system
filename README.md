# tasks_management_system

Для локального запуска проекта достаточно запустить main в основном spring классе
TaskManagementSystemApplication.

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
http://localhost:8081/swagger-ui.html соответственно.

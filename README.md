# 📬 Telegram Sender

Spring Boot приложение для отправки сообщений пользователям Telegram через REST API и Telegram Bot.

---

[![Java](https://img.shields.io/badge/Java-17-blue)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white)](https://gradle.org/)

---

## 🚀 Возможности

✅ JWT аутентификация и авторизация  
✅ Привязка Telegram-чата к пользователю  
✅ Отправка сообщений в Telegram  
✅ Хранение истории сообщений в H2 Database

---

## 📦 Требования

- Java 17+
- Gradle 8.x
- Telegram Bot API токен

---

## ⚙️ Установка и запуск

1️⃣ **Клонируй репозиторий**

```
git clone https://github.com/MonkeyQuinn/telegram_sender.git
cd telegram_sender
```

2️⃣ **Скопируй файл окружения**

```
cp .env.example .env
```

3️⃣ **Заполни свои переменные в .env**

| Переменная         | Описание                              | Пример                          |
|--------------------|---------------------------------------|---------------------------------|
| `DB_URL`           | URL к базе данных                     | `jdbc:h2:file:./data/sender_db` |
| `DB_USERNAME`      | Логин для базы                        | `sa`                            |
| `DB_PASSWORD`      | Пароль для базы                       | `sa`                            |
| `JWT_SECRET`       | Секрет для генерации JWT              | `super_secret_key`              |
| `API_INTERNAL_URL` | URL для внутренних вызовов бота к API | `http://localhost:8080`         |
| `BOT_TOKEN`        | Токен Telegram-бота                   | `123456:ABC-DEF1234`            |
| `BOT_USERNAME`     | Username бота                         | `your_bot_username`             |

4️⃣ **Собери и запусти приложение**

```
./gradlew build
./gradlew bootRun
```

## 🧪 Тестирование API

Для быстрого тестирования используй готовую коллекцию:  
👉 [Postman Collection](https://www.postman.com/monkeyquinn/workspace/pets/collection/28774402-ed2bb452-ae9e-4db2-8828-ecdc28edf3c8?action=share&source=copy-link&creator=28774402)

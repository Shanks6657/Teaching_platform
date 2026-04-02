# study - Java Web Teaching Platform Demo

A minimal Spring Boot + Thymeleaf demo for a teaching platform auth flow:
- Register (`/register`): username + password + role (`student`/`teacher`)
- Login (`/login`): validates credentials
- Home (`/home`): shows current user info after login

## Tech Stack
- Java 8
- Spring Boot 2.7.18
- Thymeleaf

## Quick Start
```powershell
mvn clean compile
mvn spring-boot:run
```

Then open:
- `http://localhost:8080/register`
- `http://localhost:8080/login`

## Current Behavior
- User data is stored in-memory (`HashMap`) in `UserController`.
- Registration checks: non-empty username/password, username length 3-20, password length 6-32, role whitelist.
- Login stores user in `HttpSession`; `/home` requires session login.
- `/logout` clears session.

## Key Files
- `src/main/java/org/example/TeachingPlatformApplication.java`
- `src/main/java/org/example/controller/UserController.java`
- `src/main/resources/templates/register.html`
- `src/main/resources/templates/login.html`
- `src/main/resources/templates/home.html`
- `src/main/resources/static/css/cyberpunk.css`


# LMSpro

This repository contains a simple LMS example composed of a Spring Boot backend and a React + TypeScript front‑end.

## Development

### Front‑end

```
npm run dev
```

### Back‑end

```
./gradlew -p demo bootRun
```

The backend requires PostgreSQL running on `localhost:5432` with the `postgres` user and password `1234` as configured in `demo/src/main/resources/application.yml`.
Passwords are stored with BCrypt. Hash your password before inserting a user; for example `1234` becomes `$2b$12$ktgSjVNNiu5i.hFkn89B3OGWRTJXBHHevz3IKQCnTsxKcAvk7GN12`.

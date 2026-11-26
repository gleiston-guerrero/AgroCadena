# Base de datos (PostgreSQL 16 + TimescaleDB)

Scripts del capítulo 2 (listados `lst:agro2-bd` y `lst:agro2-funcs`) más datos de demostración.

| Orden | Script | Cuándo |
|---|---|---|
| 1 | `01-crear-base.sql` | Una sola vez, como usuario `postgres`. Con Docker Compose no hace falta: el contenedor crea la base y el usuario a partir de `.env`. |
| 2 | — | Arrancar el *backend* una vez: Hibernate crea las tablas `contenedor`, `dispositivo`, `lectura` y `alerta`. |
| 3 | `02-funciones-y-triggers.sql` | Convierte `lectura` en hipertabla y crea la función y el *trigger* de excursión térmica. |
| 4 | `03-datos-demo.sql` | Registra el contenedor `CNT-007` y sus sensores. |

Con Docker Compose (desde `infra/`):

```bash
docker compose exec -T postgres psql -U agrocadena -d agrocadena < ../db/02-funciones-y-triggers.sql
docker compose exec -T postgres psql -U agrocadena -d agrocadena < ../db/03-datos-demo.sql
```

En PowerShell use `Get-Content ..\db\02-funciones-y-triggers.sql | docker compose exec -T postgres psql -U agrocadena -d agrocadena`.

**Diferencia con el libro.** TimescaleDB exige que la clave primaria incluya la columna de partición; por eso el script amplía la clave de `lectura` a `(id, instante)` antes de `create_hypertable`.

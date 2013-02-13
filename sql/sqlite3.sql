BEGIN;
CREATE TABLE "xmonpp_process" (
    "id" integer NOT NULL PRIMARY KEY,
    "start" integer NOT NULL,
    "stop" integer
)
;
CREATE TABLE "xmonpp_command" (
    "id" integer NOT NULL PRIMARY KEY,
    "thread_id" integer NOT NULL REFERENCES "xmonpp_process" ("id"),
    "parent_id" integer UNIQUE,
    "start" integer,
    "stop" integer,
    "cmdline" text NOT NULL,
    "stdin" text,
    "stdout" text,
    "stderr" text,
    "current_status" integer NOT NULL,
    "exit_status" integer
)
;
COMMIT;

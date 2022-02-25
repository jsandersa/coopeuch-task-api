TABLESPACE pg_default;

CREATE SEQUENCE public.hibernate_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.hibernate_sequence
    OWNER TO coopeuch;

CREATE TABLE public.tarea
(
    identificador bigint NOT NULL,
    descripcion character varying(255) COLLATE pg_catalog."default",
    fecha_creacion timestamp without time zone,
    vigente boolean NOT NULL,
    CONSTRAINT tarea_pkey PRIMARY KEY (identificador)
)

ALTER TABLE public.tarea
    OWNER to coopeuch;

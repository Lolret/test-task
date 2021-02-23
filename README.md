# Тестовое задание

Для фильтрации, агрегации HTTP-запросов используются библиотеки queryDSL, queryDSL-value-operators,
нативные sql-запросы,
"ручное" создание queryDSL-предикатов для точного выполнения требования с предикатами "from" и "to"
(/api/history?id=1&from=1565654400&to=1565827200)

После разворачивания проекта доступна swagger-спецификация по путям /swagger-ui.html и /v2/api-docs


Примеры запросов в файле api.http.

Сборка возможно через сборщики maven и gradle.

Для запуска необходимо:

    python
    docker, docker-compose
    java8

# Запуск
    git clone https://github.com/Lolret/test-task.git
    cd ./test-task
    python generate_sensor_data.py (Если планируется популяция базы (~2.5m объектов))
    
    docker-compose up -d
    
    gradlew bootRun
        ИЛИ
            mvnw compile spring-boot:run
            (mvnw.cmd для win-машин)

## Популяция базы:
### inside db:

    create table public.temp_json
    (
        values jsonb
    );
    
    copy public.temp_json from '/var/lib/postgresql/output.json';
    
    CREATE SEQUENCE id_seq
        START WITH 1
        INCREMENT BY 1
        MINVALUE 1
        NO MAXVALUE
        CACHE 1;
    
    INSERT INTO measure
    SELECT nextval('id_seq'),
           CAST(values -> 'time' as bigint)            as time,
           CAST(values -> 'value' as double precision) as value,
           CAST(values -> 'sensorId' as bigint)        as object_id,
           CAST(values -> 'objectId' as bigint)        as sensor_id
    FROM temp_json;
          
          
# Тестирование

## Необходимое:

    docker, docker-compose
    java8

    - Docker server version should be at least 1.6.0
    - Docker environment should have more than 2GB free disk space

## Запуск:

        gradlew test
        ИЛИ
            mvnw compile test
            (mvnw.cmd для win-машин)


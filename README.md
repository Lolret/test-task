# Тестовое задание для Сколтех

Для фильтрации, агрегации HTTP-запросов используются библиотека queryDSL-value-operators,
нативные sql-запросы,
"ручное" создание queryDSL-предикатов для точного выполнения требования с предикатами "from" и "to"
(/api/history?id=1&from=1565654400&to=1565827200)

После разворачивания проекта доступна swagger-спецификация по пути /swagger-ui.html

Swagger не настроен.

Примеры запросов в файле api.http.

Для сборки используется maven вместо gradle
(сделать сборку на gradle не удалось за приемлемое время).

Не сделано (из того что бы сделать хотелось):
- маппинг Entity<->DTO
- докеризация проекта
- полный запуск/популяция всего через docker-compose
- юнит-тесты

Для запуска необходимо:

    python
    docker, docker-compose
    java8

# Запуск

    python generate_sensor_data.py (Если планируется популяция базы)
    docker-compose up -d
    mvnw.cmd compile spring-boot:run

## Популяция базы:

### inside docker postgres container:

    apt-get update
    apt-get install jq
    cat data.json | jq -cr '.[]' | sed 's/\\[tn]//g' > output.json
    
### inside db:

        CREATE table public.temp_json (values jsonb);
        COPY public.temp_json FROM '/output.json';
    
        CREATE SEQUENCE id_seq
            START WITH 1
            INCREMENT BY 1
            MINVALUE 1
            NO MAXVALUE
            CACHE 1;
        
        INSERT INTO measure
        SELECT
            nextval('id_seq'),
        CAST(values->'time'  as bigint) as time,
        CAST(values->'value'  as double precision) as value,
          CAST(values->'sensorId'  as bigint) as object_id,
          CAST(values->'objectId'  as bigint) as sensor_id
          FROM temp_json;
          
          
# Тестирование

## Необходимое:

    docker, docker-compose
    java8

    - Docker server version should be at least 1.6.0
    - Docker environment should have more than 2GB free disk space

## Запуск:

    mvnw.cmd compile test

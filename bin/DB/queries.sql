INSERT INTO `travel_data`(
    `from`,
    `to`,
    `transportation_type`,
    `time_in_minutes`,
    `price`,
    `coin`
)
VALUES(
'something',
'something',
'something',
'something',
'something',
'something'
)

/***/

INSERT INTO travel_data(
    from
    ,to
    ,transportation_type
    ,time_in_minutes
    ,price,
    coin
    ) VALUES (
        '${from}',
        '${to}',
        '${type}',
        '${time}',
        '${price}',
        '${coin}');

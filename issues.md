# Issues 26-03-2021

1. Файлы пропускаются - кол-во записей не соответствует кол-ву пар
*hubs1.csv* - 42 (бывает чуть меньше или больше) / 64 (8 пар)
*HUBS_for_scaping.csv* - 33 / 69169 (263 пары)
Задержку ставил на все функции получения данных/записи, не помогло. Ошибок нет, программа корректно завершает работу. **Значит, дело не в записи.**
Также я убрал из функции grab все if-ы, поэтому никаких ограничений быть не должно

Есть спец. функция logMissedFile, но она задетектила только 1 пропуск и текст ошибки неизвестен:

`1 - undefined`

2. **С CSV непонятная ошибка "кодировка неверна", хотя она верна**

Она же вызывает другую ошибку: 

`TypeError [ERR_UNESCAPED_CHARACTERS]: Request path contains unescaped characters`

Мб здесь стоит копать. (ПРЕДПОЛАГАЮ, что неверный адрес уходит на сервер ром2рио, возникает ошибка и просто ничего не происходит, файл пропускается)

Кроме того, выводятся такие загадочные null-ы в консоль:

```
<...>
null
null
null
```

На сервере та же картина. Только еще и файлы пустые!

https://prnt.sc/10yc299

https://prnt.sc/10yc40q

3. Измерение оперативы и времени выполнения - выдержка (начало-середина файла performance), потому что в конце значения почти не отличаются. Файл перформанс - функция Шуламит, которую я переделал под запись инфы в файл и добавил измерение оперативной памяти

*hubs1.csv*

local

```
measureMs -> 4.5 MB is used at the moment
measureMs -> time for operation of "single grab operation": 25.964300006628036 ms
measureMs -> 4.54 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.8401999920606613 ms
measureMs -> 4.6 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.5071999877691269 ms
measureMs -> 4.63 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.4497999995946884 ms
measureMs -> 4.57 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.6070999950170517 ms
```

server
```
measureMs -> 4.15 MB is used at the moment
measureMs -> time for operation of "single grab operation": 15.74909496307373 ms
measureMs -> 4.2 MB is used at the moment
measureMs -> time for operation of "single grab operation": 1.0447949171066284 ms
measureMs -> 4.23 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.6674209833145142 ms
measureMs -> 4.26 MB is used at the moment
measureMs -> time for operation of "single grab operation": 1.6947940587997437 ms
measureMs -> 4.28 MB is used at the moment
measureMs -> time for operation of "single grab operation": 1.0995509624481201 ms
```


*HUBS_for_scaping.csv*

local

```
measureMs -> 5.08 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.4725999981164932 ms
measureMs -> 5.11 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.33869999647140503 ms
```

server

```
measureMs -> 4.5 MB is used at the moment
measureMs -> time for operation of "single grab operation": 25.964300006628036 ms
measureMs -> 4.54 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.8401999920606613 ms
measureMs -> 4.6 MB is used at the moment
measureMs -> time for operation of "single grab operation": 0.5071999877691269 ms
measureMs -> 4.63 MB is used at the moment
```


автор @thesiv95
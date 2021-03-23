# Warning
node version must be 14.x.x or lower!

If we could replace `exists` function in `helpers/putToFile.js` then we could run app on higher node version.

# Run
```
node index.js <path_to_file.csv> [<delay>]
```

*delay* - define how much time program will sleep before it connects to rom2rio website, in milliseconds, 2000 by default

# CSV format file rules
Since this app runs in test mode, you have to keep in mind some rules of working with app.

*Always* include this line in the beginning:
```
id,city,country
...,...,...
...,...,...
```

Encoding: **UTF-8 w/o BOM**

`id` must be 0,1,2... or subtract coefficient (see line *12* in `index.js` file).
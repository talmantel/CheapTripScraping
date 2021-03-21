# Warning
node version must be 14.x.x or lower!

If we could replace `exists` function in `helpers/putToFile.js` then we could run app on higher node version.

# CSV format file rules
*Always* include this line in the beginning:
```
id,city,country
...,...,...
...,...,...
```

Encoding: **UTF-8 w/o BOM**

`id` must be 0,1,2... or subtract coefficient (see line *12* in `index.js` file).
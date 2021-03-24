# Warning
node version must be 14.x.x or lower!

If we could replace `exists` function in `helpers/putToFile.js` then we could run app on higher node version.

# Run
```
node index.js <path_to_file.csv> [<delay>]
```

*delay* - define how much time program will sleep before it writes & zips parsed data, in milliseconds, 3000 by default. Delay is required because of async nature of node.js...


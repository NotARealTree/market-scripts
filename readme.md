## Tyche

![alt text](http://media.marketwire.com/attachments/201004/589216_Hoover_3D_6in.jpg "Hoover Icon")
## Hoover

**Hoover** is intended to be a standalone component of the system.
It **hoovers** components from the [zKillboard RedisQ](https://github.com/zKillboard/RedisQ) and stores them in the local mongodb for later processing.
It is seperate in order to allow changing of other system components while keeping data collection active.

**Hoover** is intended to be run using foreverJS.

## License

This project is MIT Licensed, the license can be found [here](LICENSE)

## Commands

hoover `forever hoover.js -l forever.log -o hoover.log -e hoover_err.log --minUptime 10000`
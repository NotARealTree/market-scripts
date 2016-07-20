## Tyche

## Purpose

The purpose of this project is to experiment with data collected from [zkillboard](https://zkillboard.com) in a variety of different
languages and systems, currently **Scala** and **NodeJS**. Menagerie is in fact my first try at using **Scala**.

## Hoover
![Hoover](http://media.marketwire.com/attachments/201004/589216_Hoover_3D_6in.jpg "Hoover Icon")

**Hoover** is intended to be a standalone component of the system.
It **hoovers** components from the [zKillboard RedisQ](https://github.com/zKillboard/RedisQ) and stores them in the local mongodb for later processing.
It is separate in order to allow changing of other system components while keeping data collection active.

**Hoover** is intended to be run using foreverJS.

## Menagerie
![alt text](http://lowres-picturecabinet.com.s3-eu-west-1.amazonaws.com/29/main/7/143155.jpg "Menagerie")

**Menagerie** is a component that uses simple serialization on killmails to find the most popular (read: most lost) fitting per ship
and can make them available. **Menagerie** is intended to be used as the backing for some-or-other API.

## License

This project is MIT Licensed, the license can be found [here](LICENSE)

## Commands

hoover `forever start hoover.js -l forever.log -o hoover.log -e hoover_err.log --minUptime 10000`

## Top Spot for silliest generated name

`Scientifically offensive Giraffe`

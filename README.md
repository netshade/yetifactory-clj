# yetifactory-clj
Originally taken from [clojure-netty][1], now me playing around w/ different
concepts in clojure to render webpages.  Eventually will fetch markdown data
from a database, render it into a [laser][2] template, and serve via a 
Netty handler.

TODOs littered throughout the app. Also here: Heroku support.

## Usage
    lein deps
    lein run

## Sending a message

The default port is 5000 out of respect to the original author of [clojure-netty][1] 
and also laziness but mostly laziness.

[1]: https://github.com/cymen/clojure-netty
[2]: https://github.com/Raynes/laser

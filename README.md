# yetifactory-clj
Originally taken from [clojure-netty][1], now me playing around w/ different
concepts in clojure to render webpages.  Eventually will fetch markdown data
from a database, render it into a [laser][2] template, and serve via a 
Netty handler.

TODOs littered throughout the app. Also here: Heroku support.

## Configuration

```sh
# Server side setup, set via dokku ENV, heroku env set, or similarp
export USERNAME="the-username-for-editing-content"
export PASSWORD="the-password-to-edit-content"
export DB_HOST="the-database-host"
export DB_USER="the-database-user"
export DB_PASSWORD="the-database-password"
export DB_NAME="the-database-name"

# Client side setup goes in .blog, can go in working directory or $HOME
export USERNAME="the-username-for-editing-content"
export PASSWORD="the-password-for-editing-content"
export EDITOR="your-editor-command-mayhaps subl -w"
export BLOGHOST="http://domain-of-your-blog.com"
```

## Setup


## Usage
    lein deps
    lein run

or
    foreman run

## Sending a message

The default port is 5000 out of respect to the original author of [clojure-netty][1] 
and also laziness but mostly laziness.

[1]: https://github.com/cymen/clojure-netty
[2]: https://github.com/Raynes/laser

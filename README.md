# yetifactory-clj

This is a single-author, mostly command line driven blog that renders 
Markdown from your favorite editor to a server side endpoint. It is primarily
written to serve my [yeti-factory][4] needs, which is to say that it mostly
exists to let me play with Clojure, and perhaps occasionally write a blog
post.

The command line driver is available in bin/live, and may be configured in 
a shell file avaliable in $HOME/.blog. See the configuration section for 
more details.

Originally taken from [clojure-netty][1], now me playing around w/ different
concepts in clojure to render webpages.  Eventually will fetch markdown data
from a database, render it into a [StringTemplate][2] template, and serve via a 
[Netty][3] handler.

Also here: Heroku support.

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

You'll need to create a database using the schema in `db/schema.sql`. By default 
this creates a database named "blog", change beforehand if you need it to be 
something different.

Once you've got your database created, you'll need to ensure the correct environment
variables are set for your config. Locally you'll likely just want to change 
the values in `project.clj`, which are just the defaults; in production, you'll
want to change via whatever mechanism your server has environment variables set.

## Usage

```sh
lein deps
lein run
```

or

```sh
foreman run
```

will run the server.  To manage content, you'll want to use `bin/live` in the 
following method:

```sh
bin/live list
```

will list available blog posts in CSV format `SLUG,TITLE`.

```sh
bin/live new
```

will open up `$EDITOR` with a new post, and when `$EDITOR` exits with `$?` 0,
will post the content to your blog.

```sh
bin/live edit SLUG
```

will open up `$EDITOR` with an existing post, and save it to the blog
when `$EDITOR` closes with `$?` 0.

```sh
bin/live destroy SLUG
```

will delete a post.

## Default Port

The default port is 5000 out of respect to the original author of [clojure-netty][1] 
and also laziness but mostly laziness.

[1]: https://github.com/cymen/clojure-netty
[2]: http://stringtemplate.org/
[3]: http://netty.io/
[4]: http://yeti-factory.org/

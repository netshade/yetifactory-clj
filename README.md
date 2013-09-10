# yetifactory-clj

This is a single-author, mostly command line driven blog that renders
Markdown from your favorite editor to a server side endpoint. It is primarily
written to serve my [yeti-factory][4] needs, which is to say that it mostly
exists to let me play with Clojure, and perhaps occasionally write a blog
post.

## Features:

* Programmer "friendly". Written in Lisp with what I hope is sane organization.
* [Ring][5] compatible, so you can use existing Ring middleware to wrap your requests.
* Template driven with a layout style somewhat similar to Rails. Uses [StringTemplate][2].
* Fast, thanks to [Netty][3].
* Command-line driven, so you can use your favorite `$EDITOR` to write posts.
* Automatically uploads local assets to S3 if they are referenced in the outgoing post.
* Written in short spurts over the past two weeks, so you know the software quality is high
* No tests, because laziness
* Disqus integration! Google Analytics integration! Are these features! I don't know!

## TODO:

* Better organization, ala `yetifactory.app.middleware`, etc.
* Ensure Heroku support.
* Better caching header defaults.
* Environment specific behavior, like template loading.
* Maybe? not use environment variables for config everywhere.
* RSS support probably.
* Remove yetifactory name, come up with a better name
* Remove my specific blog styling, keep in a separate branch. Maybe.
* `grep -ri 'TODO' .`
* A 500 page
* Pagination, probably for when I write > 20 blog posts

The command line driver is available in `bin/live`, and may be configured in
a shell file avaliable in `$HOME/.blog`. See the configuration section for
more details.

Originally taken from [clojure-netty][1], now me playing around w/ different
concepts in clojure to render webpages.  Eventually will fetch markdown data
from a database, render it into a [StringTemplate][2] template, and serve via a
[Netty][3] handler.


## Configuration

```sh
# Server side setup, set via dokku ENV, heroku env set, or similarp
export USERNAME="the-username-for-editing-content"
export PASSWORD="the-password-to-edit-content"
export DB_HOST="the-database-host"
export DB_USER="the-database-user"
export DB_PASSWORD="the-database-password"
export DB_NAME="the-database-name"
# Disqus can be enabled by
export DISQUS_NAME="your-disqus-name"
# Google Analytics can be enabled by
export GOOGLE_ANALYTICS_ID="your-ga-id"

# Client side setup goes in .blog, can go in working directory or $HOME
export USERNAME="the-username-for-editing-content"
export PASSWORD="the-password-for-editing-content"
export EDITOR="your-editor-command-mayhaps subl -w"
export BLOGHOST="http://domain-of-your-blog.com"

# If you want to do automatic file uploading to S3, you can use the
# included upload_image script.  You'll need the following command line
# tools installed: xmlstarlet, markdown, ruby
#
# Once you've done that, add the following to your client side script:
#
export UPLOAD_SCRIPT="./bin/upload_image"
export EC2_ACCESS_KEY="YOUR-EC2-ACCESS_KEY-FOR-S3"
export EC2_SECRET_KEY="YOUR-EC2-SECRET_KEY-FOR-S3"
#
# If you want to write your own image uploader, it has to:
# Accept a filename on STDIN, AND
# Output "LOCAL_PATH:REMOTE_PATH" on STDOUT
# Once you've done that, just use the path to your image uploader
# in UPLOAD_SCRIPT as opposed to ./bin/upload_image.


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


### Startup

```sh
lein deps
lein run
```

or

```sh
foreman run
```

will run the server.  By default content is available on port `5000`.

### List Content

To manage content, you'll want to use `bin/live` in the
following method:

```sh
bin/live list
```

will list available blog posts in CSV format `SLUG,TITLE`.

### Manage Content

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

### Notes on Deployment

I've been testing this on Dokku, which uses the Heroku Clojure buildpack. I expect
that this /will/ work on Heroku, but you'll need to set the following env vars:

```sh
export DB_PROTOCOL="postgresql"
export DB_DRIVER="org.postgresql.Driver"
```

in order to work with Postgres. You'll also need to edit project.clj to include the
Postgres driver. That is assuming you're using Postgres on Heroku; other database users
should edit the above envars to suit their specific needs.


[1]: https://github.com/cymen/clojure-netty
[2]: http://stringtemplate.org/
[3]: http://netty.io/
[4]: http://yeti-factory.org/
[5]: https://github.com/ring-clojure/ring

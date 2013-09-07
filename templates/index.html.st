<!DOCTYPE html>
<html lang="en">
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Yeti Factory</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet" />
    <link href="/css/yetifactory.css" rel="stylesheet" />
  </head>
  <body>
    <nav class="navbar navbar-default navbar-static-top" role="navigation">
      <div class="navbar-header">
        <h3>
          <img src="/icon32.png" margin="0" border="0"/>
          yetifactory
        </h3>
      </div>
    </nav>
    <section>
      <div class="row">
        <div class="content col-md-10">
          $posts:{post|
            <article>
              <b>$post.title$</b>
            </article>
          }$
        </div>
        <div class="sidebar col-md-2">
          <div class="sidebar-content">
            <h4>about</h4>
            <p>
              This is Chris Zelenak's personal blog. I work at <a href="http://fastestforward.com/">Fastest Forward</a> on consulting and our product, <a href="https://instrumentalapp.com/">Instrumental</a>. This blog is served via a personal project using <a href="http://clojure.org/">Clojure</a> and <a hreff="http://netty.io">Netty</a>, and is <a href="https://bitbucket.org/netshade/yetifactory-clj">open source</a>.
            </p>
            <a href="https://github.com/netshade"><img src="/github48.png" /></a>
            <a href="https://twitter.com/netshade"><img src="/twitter48.png" /></a>
          </div>
        </div>
      </div>
    </section>
    <script type="text/javascript" src="/js/jquery-2.0.3.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/app.js"></script>
  </body>
</html>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Yeti Factory</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet" />
    <link href="/css/yetifactory.css" rel="stylesheet" />
    <link href="/css/gfm.css" rel="stylesheet" />
    <link rel="alternate" type="application/rss+xml" title="RSS" href="http://yeti-factory.org/feed.rss" />
  </head>
  <body>
    <nav class="navbar navbar-default navbar-static-top" role="navigation">
      <div class="navbar-header">
        <h3>
          <a href="/">
            <img src="/icon32.png" margin="0" border="0"/>
            yetifactory
          </a>
        </h3>
      </div>
    </nav>
    <section>
      <div class="row">
        <div class="content col-md-10">
$__content__$
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

    $if(google_analytics_id)$
    <script type="text/javascript">

      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', '$google_analytics_id$']);
      _gaq.push(['_trackPageview']);

      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();

    </script>
    $endif$
  </body>
</html>

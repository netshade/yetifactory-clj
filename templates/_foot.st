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

$posts:{post|
  <section>
    <heading>
      <h3><a href="/post/$post.slug$">$post.title$</a></h3>
      <p>
        <time datetime="$post.created_at$">
          $post.created_at_ago$
        </time>
      </p>
    </heading>
    <p class="snippet">
$post.snippet$
    </p>
    <a href="/post/$post.slug$#disqus_thread">
      $if(post.truncated)$
        Read more...
      $else$
        Comment
      $endif$
    </a>
  </section>
}$

$if(disqus_name)$
<script type="text/javascript">
/* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
var disqus_shortname = '$disqus_name$'; // required: replace example with your forum shortname

/* * * DON'T EDIT BELOW THIS LINE * * */
(function () {
    var s = document.createElement('script'); s.async = true;
    s.type = 'text/javascript';
    s.src = '//' + disqus_shortname + '.disqus.com/count.js';
    (document.getElementsByTagName('HEAD')[0] || document.getElementsByTagName('BODY')[0]).appendChild(s);
}());
</script>
$endif$

$posts:{post|
  <section>
    <heading>
      <h3><a href="/post/$post.id$">$post.title$</a></h3>
      <p>
        <time datetime="$post.created_at$">
          $post.created_at_ago$
        </time>
      </p>
    </heading>
    <p class="snippet">
      $post.body_snippet$
    </p>
  </section>
}$

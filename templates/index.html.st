<!DOCTYPE html>
<html lang="en">
  <head>
  $_head()$
  </head>
  <body>
    $_navigation()$
    <section>
      <div class="row">
        <div class="content col-md-10">
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
        </div>
        $_sidebar()$
      </div>
    </section>
    $_foot()$
  </body>
</html>

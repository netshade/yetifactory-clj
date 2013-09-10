<?xml version="1.0" encoding="ISO-8859-1" ?>
<rss version="2.0">
  <channel>
    <title>Yeti Factory</title>
    <link>http://yeti-factory.org</link>
    <description>personal blog of chris zelenak</description>
    $posts:{post|
      <item>
        <title>$post.title$</title>
        <guid isPermaLink="false">http://yeti-factory.org/post/$post.slug$</guid>
        <link>http://yeti-factory.org/post/$post.slug$</link>
        <pubDate>$post.pub_date$</pubDate>
        <description>
          <![CDATA[
            $post.snippet$
          ]]>
        </description>
      </item>
    }$
  </channel>
</rss>

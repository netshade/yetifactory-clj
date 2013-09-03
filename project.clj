(defproject yetifactory.core "0.0.1-SNAPSHOT"
  :description "Basic example of using netty with Clojure"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [io.netty/netty-all "4.0.8.Final"]
                 [markdown-clj "0.9.31"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-devel "1.2.0"]
                 [me.raynes/laser "0.1.12"]]
  :java-source-paths ["src/main/java"]
  :main yetifactory.core)

(defproject yetifactory.core "0.0.1-SNAPSHOT"
  :description "Basic example of using netty with Clojure"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [io.netty/netty-all "4.0.8.Final"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-devel "1.2.0"]
                 [org.antlr/ST4 "4.0.7"]
                 [org.clojure/java.jdbc "0.3.0-alpha4"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [com.mchange/c3p0 "0.9.2.1"]
                 [org.ocpsoft.prettytime/prettytime "3.0.2.Final"]]
  :java-source-paths ["src/main/java"]
  :main yetifactory.core)

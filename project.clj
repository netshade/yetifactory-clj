(defproject yetifactory.core "0.0.1-HAIRY"
  :description "Baby's first clojure app"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [io.netty/netty-all "4.0.8.Final"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-devel "1.2.0"]
                 [org.antlr/ST4 "4.0.7"]
                 [org.clojure/java.jdbc "0.3.0-alpha4"]
                 [org.clojure/core.match "0.2.0-rc5"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [com.mchange/c3p0 "0.9.2.1"]
                 [org.ocpsoft.prettytime/prettytime "3.0.2.Final"]
                 [clj-http "0.7.6"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/data.json "0.2.3"]
                 [environ "0.4.0"]]
  :java-source-paths ["src/main/java"]
  :plugins [[lein-environ "0.4.0"]]
  :main yetifactory.core
  :profiles { :user {
    :db-host          "127.0.0.1:3306"
    :db-user          "root"
    :db-password      nil
    :db-name          "blog"
    :db-protocol      "mysql"
    :db-driver        "com.mysql.jdbc.Driver"
    :auth-user        "test"
    :auth-password    "test"
    :port             5000
  }})

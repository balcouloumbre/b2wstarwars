(defproject b2w.starwars "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [metosin/compojure-api "1.1.11"]
                 [ring/ring-defaults "0.3.2"]
                 [metosin/schema-tools "0.11.0"]
                 [com.novemberain/monger "3.1.0"]
                 [clj-http "3.10.0"]
                 [cheshire "5.8.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler b2w.starwars.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})

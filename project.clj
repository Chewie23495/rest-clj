(defproject webapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.3.4"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.2"]
                 [ring "1.7.0"]
                 [org.clojure/data.json "1.0.0"]
                 [ring/ring-json "0.5.0"]
                 [prismatic/schema "1.1.12"]
                 [ring-cors "0.1.13"]]
  :repl-options {:init-ns webapp.core}
  :main webapp.core
  :uberjar-name "webapp.jar")
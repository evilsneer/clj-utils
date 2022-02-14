(defproject emptyone/utils "1.0.10"
  :description "Clojure useful utils"
  :url "https://github.com/evilsneer/clj-utils"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[buddy/buddy-core "1.10.413"]]
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.3"]]}}
  :deploy-repositories [["releases" {:sign-releases false :url "https://clojars.org/repo"}]
                        ["snapshots" {:sign-releases false :url "https://clojars.org/repo"}]]
  :aliases {"update-readme-version" ["shell" "sed" "-i" "s/\\\\[emptyone\\\\/utils \"[0-9.]*\"\\\\]/[emptyone\\\\/utils \"${:version}\"]/" "README.md"]
            "upg"                   ["ancient" "upgrade" ":interactive" ":no-tests" ":check-clojure"]}
  :release-tasks ^:replace [["shell" "git" "diff" "--exit-code"]
                            ["vcs" "assert-committed"]
                            ["change" "version" "leiningen.release/bump-version"]
                            ["change" "version" "leiningen.release/bump-version" "release"]
                            ["changelog" "release"]
                            #_["update-readme-version"]
                            ["vcs" "commit"]
                            ["vcs" "tag" "--no-sign"]
                            ["deploy"]
                            ["vcs" "push"]])

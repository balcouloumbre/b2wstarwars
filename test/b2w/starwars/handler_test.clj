(ns b2w.starwars.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [b2w.starwars.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "B2W Star Wars Planets"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(ns b2w.starwars.core-test
  (:require [clojure.test :refer :all])
  (:use b2w.starwars.core))

(deftest create-new-planet-test
  (let [planet (create-new-planet "Alderaan" "Temperate" "Mountain")]
    (is (not (nil? planet)))
    (is (= (:id planet) 1))
    (is (= (:name planet) "Alderaan"))
    (is (= (:climate planet) "Temperate"))
    (is (= (:terrain planet) "Mountain"))))

(deftest create-new-planet-empty-name-test
  (is (thrown? AssertionError (create-new-planet "" "Temperate" "Mountain"))))

(deftest create-new-planet-nil-name-test
  (is (thrown? AssertionError (create-new-planet nil "Temperate" "Mountain"))))
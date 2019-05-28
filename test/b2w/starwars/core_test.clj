(ns b2w.starwars.core-test
  (:require [clojure.test :refer :all])
  (:use b2w.starwars.core))

(deftest create-new-planet-test
  (let [planet (create-new-planet 1 "Alderaan" "Temperate" "Mountain")]
    (is (not (nil? planet)))
    (is (= (:id planet) 1))
    (is (= (:name planet) "Alderaan"))
    (is (= (:climate planet) "Temperate"))
    (is (= (:terrain planet) "Mountain"))))

(deftest create-new-planet-empty-name-test
  (is (thrown? AssertionError (create-new-planet 1 "" "Temperate" "Mountain"))))

(deftest create-new-planet-nil-name-test
  (is (thrown? AssertionError (create-new-planet 1 nil "Temperate" "Mountain"))))

(deftest update-planet-test
  (let [updated-planet
        (update-planet
          (create-new-planet 1 "Alderaan" "Temperate" "Mountain") "Tatooine" "Arid" "Desert")]
    (is (not (nil? updated-planet)))
    (is (= (:id updated-planet) 1))
    (is (= (:name updated-planet) "Tatooine"))
    (is (= (:climate updated-planet) "Arid"))
    (is (= (:terrain updated-planet) "Desert"))))
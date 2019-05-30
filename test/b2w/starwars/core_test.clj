(ns b2w.starwars.core-test
  (:require [clojure.test :refer :all])
  (:use b2w.starwars.core))

;create-new-planet
(deftest create-new-planet-test
  (let [planet (create-new-planet "Alderaan" "Temperate" "Mountain")]
    (is (not (nil? planet)))
    (is (= (:name planet) "Alderaan"))
    (is (= (:climate planet) "Temperate"))
    (is (= (:terrain planet) "Mountain"))))

(deftest create-new-planet-empty-name-test
  (is (thrown? AssertionError (create-new-planet "" "Temperate" "Mountain"))))

(deftest create-new-planet-nil-name-test
  (is (thrown? AssertionError (create-new-planet nil "Temperate" "Mountain"))))

;add-films-to-planet
(deftest add-films-to-planet-test
  (let [planet (->PersistedPlanet "2f6fab2d07e141ceb3e9afba6f2c571c" "Tatooine" "Arid" "Desert")]
  (is(= 5 (:films (add-films-to-planet planet 5))))))
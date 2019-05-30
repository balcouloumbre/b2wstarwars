(ns b2w.starwars.service-test
  (:require [clojure.test :refer :all]
            [b2w.starwars.core :as core]
            [b2w.starwars.service :as service]))

(defrecord fake-planet-repository []
  core/planet-repository
  (insert-planet
    [this planet]
    (-> (core/map->PersistedPlanet (assoc planet :id "5cec324c243ad449886929e1"))))
  (delete-planet
    [this id]
    (-> 1))
  (update-planet
    [this planet]
    (-> 1))
  (find-planet
    [this id]
    (core/->PersistedPlanet "5cec324c243ad449886929e1" "Tatooine" "Arid" "Desert"))
  (list-planets
    [this max]
    (list (core/->PersistedPlanet "5cec324c243ad449886929e1" "Tatooine" "Arid" "Desert")
          (core/->PersistedPlanet "5cec32da243ad449886929e4" "Alderaan" "Temperate" "Mountain"))))

(def test-repository (->fake-planet-repository))

(defn fake-external-api
  [planet]
  (list "Film1" "Film2"))

;create-planet
(deftest create-planet-test
  (let [persisted-planet (service/create-planet (hash-map :name "Tatooine" :climate "Arid" :terrain "Desert") test-repository)]
  (is (not (nil? persisted-planet)))
  (is (= "5cec324c243ad449886929e1" (:id persisted-planet)))
  (is (= "Tatooine" (:name persisted-planet)))
  (is (= "Arid" (:climate persisted-planet)))
  (is (= "Desert" (:terrain persisted-planet)))))

;list-planets
(deftest list-planet-test
  (let [planet-list (service/list-planets test-repository fake-external-api)]
    (is (not (nil? planet-list)))
    (is (= 2 (count planet-list)))
      (is (and
         (= "Tatooine" (:name (first planet-list)))
          (= 2 (:films (first planet-list)))))
    (is (and
          (= "Alderaan" (:name (last planet-list)))
          (= 2 (:films (last planet-list)))))))

;find-planet
(deftest find-planet-test
  (let [persisted-planet (service/find-planet "5cec324c243ad449886929e1" test-repository fake-external-api)]
    (is (not (nil? persisted-planet)))
    (is (= "5cec324c243ad449886929e1" (:id persisted-planet)))
    (is (= "Tatooine" (:name persisted-planet)))
    (is (= "Arid" (:climate persisted-planet)))
    (is (= "Desert" (:terrain persisted-planet)))))

;delete-planet
(deftest delete-planet-test
  (is(= 1 (service/delete-planet "5cec324c243ad449886929e1" test-repository))))

;update-planet
(deftest update-planet-test
  (is(= 1 (service/update-planet (core/->PersistedPlanet "5cec324c243ad449886929e1" "Tatooine" "Arid" "Desert") test-repository))))

;add-film-count
(deftest add-film-count-test
  (let [new-planet (core/->PersistedPlanet "5cec324c243ad449886929e1" "Tatooine" "Arid" "Desert")]
  (is (= 2 (:films (service/add-film-count new-planet fake-external-api))))))
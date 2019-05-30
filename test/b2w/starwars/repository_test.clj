(ns b2w.starwars.repository-test
  (:require [clojure.test :refer :all]
            [b2w.starwars.core :as core]
            [b2w.starwars.repository :as rep])
  (:import  (org.bson.types ObjectId)))

(def db-name "b2wstarwars")
(def coll-name "Planets")

(def a-domain-planet (core/->PersistedPlanet "5ce43f24243ad436d0d1b5c7" "Tatooine" "Arid" "Desert"))

(def a-db-planet
  (dissoc (assoc a-domain-planet :_id (ObjectId. "5ce43f24243ad436d0d1b5c7")) :id))

;domain-planet->db-planet
(deftest domain-planet->db-planet
  (let [converted-planet (rep/domain-planet->db-planet a-domain-planet)]
    (is (not (nil? converted-planet)))
    (is (nil? (converted-planet :id)))
    (is (not (nil? (converted-planet :_id))))))


;db-planet->domain-planet
(deftest db-planet->domain-planet
  (let [converted-planet (rep/db-planet->domain-planet a-db-planet)]
    (is (not (nil? converted-planet)))
    (is (nil? (converted-planet :_id)))
    (is (not (nil? (converted-planet :id))))))

;insert-new-record-find-remove
(deftest insert-record-remove-record
  (let [result (rep/insert-new-record db-name coll-name (core/->NewPlanet "Tatooine" "Arid" "Desert"))]
    (is (not (nil? result)))
    (is (not (nil? (:id result))))
    (is (= "Tatooine" (:name result)))
    (is (= "Arid" (:climate result)))
    (is (= "Desert" (:terrain result)))
    (let [id (:id result)]
    (rep/delete-by-id db-name coll-name id)
    (is (nil? (rep/find-by-id db-name coll-name id))))))
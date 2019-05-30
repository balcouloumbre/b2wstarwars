(ns b2w.starwars.service
  (:require [b2w.starwars.repository :as rep]
            [b2w.starwars.core :as core]
            [clj-http.client :as http]))

; Using localhost, default port. And leaving the database and collection hardcoded for now.
(def mongo-rep (rep/->mongo-planet-repository "b2wstarwars" "Planets"))

(defn delete-planet
  [id repository]
  (core/delete-planet repository id))

(defn create-planet
  [planet repository]
  (core/insert-planet repository
    (core/create-new-planet (planet :name) (planet :climate) (planet :terrain))))

(defn update-planet
  [planet repository]
  (core/update-planet repository planet))

(defn add-film-count
  [planet external-api]
  (core/add-films-to-planet planet (count (external-api(get planet :name)))))

(defn find-planet
  [id repository external-api]
  (let [planet (core/find-planet repository id)]
    (if(not(nil? planet))
      (add-film-count planet external-api))))

(defn list-planets
  [repository external-api]
  (map
    (fn [planet] (add-film-count planet external-api))
      (core/list-planets repository 30)))



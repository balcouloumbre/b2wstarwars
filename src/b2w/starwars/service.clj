(ns b2w.starwars.service
  (:require [b2w.starwars.repository :as rep]
            [b2w.starwars.core :as core]
            [clj-http.client :as http]))

;; Using localhost, default port. And leaving the database and collection hardcoded for now.
(def mongo-rep (rep/->mongo-planet-repository "b2wstarwars" "Planets"))

(defn delete-planet
  [id]
  (core/delete-planet mongo-rep id))

(defn get-films-from-external-api
  [name]
  (let [external-planets (-> (http/get (str "https://swapi.co/api/planets/?search=" name))
      :body
      (cheshire.core/parse-string true)
      :results)]
      (if (or (nil? external-planets) (not= 1 (count external-planets)))
        []
        ((first external-planets) :films))))

;Memoize approach for caching SWAPI planets. Fast and easy, but since an external API isn't pure or transparent
;Would have been a better idea to store those results in an external cache like Redis, with an expiration timer.
(def get-films-from-external-api-memo
  (memoize get-films-from-external-api))

(defn create-planet
  [planet]
  (core/insert-planet mongo-rep
    (core/create-new-planet (planet :name) (planet :climate) (planet :terrain))))

(defn update-planet
  [planet]
  (core/update-planet mongo-rep planet))

(defn add-film-count
  [planet]
  (core/add-films-to-planet planet (count (get-films-from-external-api-memo(planet :name)))))

(defn find-planet
  [id]
  (let [planet (core/find-planet mongo-rep id)]
    (if(not(nil? planet))
      (add-film-count planet))))

(defn list-planets
  []
  (map
    (fn [planet] (add-film-count planet))
      (core/list-planets mongo-rep 30)))



(ns b2w.starwars.service
  (:require [b2w.starwars.repository :as rep]
            [b2w.starwars.core :as core]
            [clj-http.client :as http]))

(defn remove-by-id
  [id]
  (rep/delete-planet id))

(defn get-planet-from-external-api
  [name]
  (-> (http/get (str "https://swapi.co/api/planets/?search=" name))
      :body
      (cheshire.core/parse-string true)
      :results))

;Memoize approach for caching SWAPI planets. Fast and easy, but since an external API isn't pure or transparent
;Would have been a better idea to store those results in an external cache like Redis, with an expiration timer.
(def get-planet-from-external-api-memo
  (memoize get-planet-from-external-api ))

(defn get-planet-film-count
  [name]
  (let [result (get-planet-from-external-api-memo name)]
    (if (or (nil? result) (not= 1 (count result)))
      0
      (count ((first result) :films)))))

(defn create-planet
  [planet]
  (rep/insert-planet
    (core/create-new-planet (planet :name) (planet :climate) (planet :terrain))))

(defn add-film-count
  [planet]
  (core/add-films-to-planet planet (get-planet-film-count(planet :name))))

(defn find-by-id
  [id]
  (let [planet (rep/find-planet id)]
    (if(not(nil? planet))
      (add-film-count planet))))


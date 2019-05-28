(ns b2w.starwars.core
  (:require [clojure.string :as str]))

(defprotocol planet-repository "Protocol for a planet repository"
  (insert-planet [planet] "inserts a new planet into the db")
  (update-planet [planet] "updates an existing planet with new data")
  (delete-planet [id] "deletes a planet with a given id")
  (find-planet [id] "finds a planet with a given id"))

(defrecord NewPlanet [name climate terrain])
(defrecord PersistedPlanet [id name climate terrain])

(defn create-new-planet
  [name climate terrain]
  {:pre [(not (str/blank? name))]}
  (NewPlanet. name climate terrain))

(defn add-films-to-planet
  [planet films]
  (assoc planet :films films))
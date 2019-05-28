(ns b2w.starwars.core
  (:require [clojure.string :as str]))

(defprotocol planet-repository "Protocol for a planet repository"
  (insert-planet [this planet] "Inserts a new planet into the db")
  (update-planet [this planet] "Updates an existing planet with new data")
  (delete-planet [this id] "Deletes a planet with a given id")
  (find-planet [this id] "Finds a planet with a given id")
  (list-planets [this max] "Lists all planets"))

(defrecord NewPlanet [name climate terrain])
(defrecord PersistedPlanet [id name climate terrain])

(defn create-new-planet
  [name climate terrain]
  {:pre [(not (str/blank? name))]}
  (NewPlanet. name climate terrain))

(defn add-films-to-planet
  [planet films]
  (assoc planet :films films))
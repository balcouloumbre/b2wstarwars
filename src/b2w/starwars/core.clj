(ns b2w.starwars.core
  (:require [clojure.string :as str]))

(defrecord NewPlanet [name climate terrain])
(defrecord PersistedPlanet [id name climate terrain])

(defn create-new-planet
  [name climate terrain]
  {:pre [(not (str/blank? name))]}
  (NewPlanet. name climate terrain))

(defn add-films-to-planet
  [planet films]
  (assoc planet :films films))
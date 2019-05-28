(ns b2w.starwars.core
  (:require [clojure.string :as str]))

(defrecord NewPlanet [name climate terrain])
(defrecord PersistedPlanet [id name climate terrain])

(defn create-new-planet
  [name climate terrain]
  {:pre [(not (str/blank? name))]}
  (NewPlanet. name climate terrain))

(defn create-persisted-planet
  [id name climate terrain]
  {:pre [(not (str/blank? name))]}
  (PersistedPlanet. id name climate terrain))

(defn update-planet
  [p name climate terrain]
  (PersistedPlanet. (get p :id) name climate terrain))
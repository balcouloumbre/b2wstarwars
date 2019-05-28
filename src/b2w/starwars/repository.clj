(ns b2w.starwars.repository
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [b2w.starwars.core :as core])
  (:import [com.mongodb MongoOptions ServerAddress]
           (org.bson.types ObjectId)))

;; Using localhost, default port. And leaving the database and collection hardcoded for now.

(defn domain-planet->db-planet [planet]
  (let [id (:id planet)]
    (-> planet
        (dissoc :id)
        (assoc :_id (ObjectId. id)))))

(defn db-planet->domain-planet [planet]
  (if(not(nil? planet))
  (let [id (:_id planet)]
    (-> planet
        (dissoc :_id)
        (assoc :id (str id))))))

(defn insert-planet
  [planet]
  (let [conn (mg/connect)
        db   (mg/get-db conn "b2wstarwars")
        coll "Planets"]
    (db-planet->domain-planet
      (mc/insert-and-return db coll planet))))

(defn update-planet
[planet]
(let [conn (mg/connect)
      db   (mg/get-db conn "b2wstarwars")
      coll "Planets"
      db-planet (domain-planet->db-planet(planet))]
  (mc/update-by-id db coll (get db-planet :_id) db-planet)))

(defn delete-planet
  [id]
  (let [conn (mg/connect)
       db   (mg/get-db conn "b2wstarwars")
       coll "Planets"]
    (mc/remove-by-id db coll (ObjectId. id))))

(defn find-planet
  [id]
  (let [conn (mg/connect)
        db   (mg/get-db conn "b2wstarwars")
        coll "Planets"]
    (db-planet->domain-planet (mc/find-map-by-id db coll (ObjectId. id)))))
(ns b2w.starwars.repository
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [b2w.starwars.core :as core])
  (:import [com.mongodb MongoOptions ServerAddress]
           (org.bson.types ObjectId)))

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

(defn insert-new-record
  [db coll planet]
  (let [conn (mg/connect)
        mongo   (mg/get-db conn db)]
    (db-planet->domain-planet
      (mc/insert-and-return mongo coll planet))))

(defn delete-by-id
  [db coll id]
  (let [conn (mg/connect)
        mongo   (mg/get-db conn db)]
    (mc/remove-by-id mongo coll (ObjectId. id))))

(defn update-record
[db coll planet]
(let [conn (mg/connect)
      mongo   (mg/get-db conn db)
      db-planet (domain-planet->db-planet planet)]
  (mc/update-by-id mongo coll (db-planet :_id) db-planet)))

(defn find-by-id
  [db coll id]
  (let [conn (mg/connect)
        mongo   (mg/get-db conn db)]
    (db-planet->domain-planet (mc/find-map-by-id mongo coll (ObjectId. id)))))

(defn list-records
  [db coll max]
  (let [conn (mg/connect)
         mongo   (mg/get-db conn db)]
    (map db-planet->domain-planet (take max (mc/find-maps mongo coll)))))

(defrecord mongo-planet-repository [db coll]
  core/planet-repository
  (insert-planet
    [this planet]
    (insert-new-record db coll planet))
  (delete-planet
    [this id]
    (delete-by-id db coll id))
  (update-planet
    [this planet]
    (update-record db coll planet))
  (find-planet
    [this id]
    (find-by-id db coll id))
  (list-planets
  [this max]
  (list-records db coll max)))
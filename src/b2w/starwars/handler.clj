(ns b2w.starwars.handler
  (:require [compojure.api.sweet :refer :all]
            [clj-http.client :as http]
            [ring.util.http-response :refer :all]
            [schema.core :as schema]
            [b2w.starwars.core :as core]
            [b2w.starwars.repository :as rep]))

(defn get-planet-from-external-api
  [name]
  (let [response (http/get (str "https://swapi.co/api/planets/?search=" name))
        body (cheshire.core/parse-string (:body response) true)]
    (:results body)))

(defn get-planet-film-count
  [name]
  (let [result (get-planet-from-external-api name)]
    (if (or (nil? result) (not= 1 (count result)))
           0
           (count ((first result) :films)))))

(defn remove-by-id
  [id]
  (ok (rep/delete-planet id)))

(defn add-film-count
  [planet]
  (assoc planet :films (get-planet-film-count(planet :name))))

(defn find-by-id
  [id]
  (let [planet (rep/find-planet id)]
    (if(not(nil? planet))
      (ok (add-film-count planet))
      (not-found))))

(defn create-planet
  [planet]
  (rep/insert-planet
      (core/create-new-planet (planet :name) (planet :climate) (planet :terrain))))

(schema/defschema Planet
             {:id schema/Str
              :name schema/Str
              (schema/optional-key :films) schema/Int
              (schema/optional-key :climate) schema/Str
              (schema/optional-key :terrain) schema/Str})

(schema/defschema NewPlanet
             {:name schema/Str
              (schema/optional-key :climate) schema/Str
              (schema/optional-key :terrain) schema/Str})

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "B2W SW Planets API"
                    :description "An API for Star Wars Planets."}
             :tags [{:name "api", :description "Planets API"}]}}}

    (context "/api" []
      :tags ["api"]
      (GET "/planets/:id" [id]
        :return Planet
        :summary "Gets a planet with that Id"
        (find-by-id id))
      (DELETE "/planets/:id" [id]
        :summary "Removes a planet with that Id"
        (remove-by-id id))
      (POST "/planets" []
        :return Planet
        :body [planet NewPlanet]
        :summary "Creates a new planet"
        (ok (create-planet planet)))
      )))






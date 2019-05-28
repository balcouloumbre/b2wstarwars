(ns b2w.starwars.handler
  (:require [compojure.api.sweet :refer :all]
            [clj-http.client :as http]
            [ring.util.http-response :refer :all]
            [schema.core :as schema]
            [b2w.starwars.service :as service]))

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
        (let [planet (service/find-by-id id)]
                      (if(nil? planet)
                        not-found
                        (ok planet))))
      (DELETE "/planets/:id" [id]
        :summary "Removes a planet with that Id"
        (ok (service/remove-by-id id)))
      (POST "/planets" []
        :return Planet
        :body [planet NewPlanet]
        :summary "Creates a new planet"
        (ok (service/create-planet planet)))
      )))
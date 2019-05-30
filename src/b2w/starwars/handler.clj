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

; Using hardcoded url for SWAPI.
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
      (GET "/planets/" []
        :summary "List planets."
        (ok (service/list-planets service/mongo-rep get-films-from-external-api-memo)))
      (GET "/planets/:id" [id]
        :return (schema/maybe Planet)
        :summary "Gets a planet with that Id"
        (let [planet (service/find-planet id service/mongo-rep get-films-from-external-api-memo)]
                      (if(nil? planet)
                        (not-found)
                        (ok planet))))
      (DELETE "/planets/:id" [id]
        :summary "Removes a planet with that Id"
        (service/delete-planet id service/mongo-rep)
        (ok "Success!"))
      (PUT "/planets/" []
        :body [planet Planet]
        :summary "Updates a planet with new data"
        (service/update-planet planet service/mongo-rep)
        (ok "Success!"))
      (POST "/planets" []
        :return Planet
        :body [planet NewPlanet]
        :summary "Creates a new planet"
        (ok (service/create-planet planet service/mongo-rep)))
      )))
(ns cleito.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [cleito.middleware.formats :as formats]
    [cleito.middleware.exception :as exception]
    [cleito.pow :as pow]
    [ring.util.http-response :refer :all]
    [clojure.java.io :as io]))

(defn service-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
    :swagger {:id ::api}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 exception/exception-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc true
        :swagger {:info {:title "Proof of Work Client Attestation"
                         :description "https://github.com/rafaelfcsouza/cleito"}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ["/work"
    {:swagger {:tags ["pow"]}}

    ["/"
     {:get {:summary "requests a challenge to be worked on"
            :responses {200 {:body {:work string?, :difficulty int?}}}
            :handler (fn [_]
                       {:status 200
                        :body {:work (pow/new-work)
                               :difficulty pow/difficulty}})}}]

    ["/:work/proof"
     {:post {:summary "submit proof of work"
             :parameters {:body {:proof string?, :nonce int?}
                          :path {:work string?}}
             :handler (fn [{{{:keys [proof nonce]} :body {:keys [work]} :path} :parameters}]
                        (if (pow/valid-proof? proof work nonce)
                          {:status 200}
                          {:status 400}))}}]]])
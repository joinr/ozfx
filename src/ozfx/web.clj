(ns ozfx.web
  (:require [fx-clj.core :as fx]
            [freactive.core :as fr])
  (:import [main.java.com.goxr3plus.javafxwebbrowser.browser WebBrowserController]))

(defn create-view []
  (fx/h-box
    (fx/button {:on-action (fn [e] (println "Hello World!"))
                :text "Click Me!"})))

(defn browser [ & {:keys [init-url]}]
  (let [wv     (fx/web-view)
        we     (.getEngine wv)
        address-bar (fx/text-field :#input)
        url    (fr/atom (or init-url ""))
        tprop       (.textProperty address-bar)
        ;_ (println (fx-clj.core.extensibility/convert-arg javafx.beans.property.Property tprop nil))
        ;_      (fx/bind-> tprop url)
        load!  (fn [& args] (.load we @url))
        _      (when init-url
                 (do
                   (.setText address-bar init-url)
                   (.load we init-url)))
        load-btn (fx/button :#reload-btn {:text "Load Page"
                                          :on-action load!})]
    {:webview wv
     :address-bar address-bar
     :url url
     :load-btn load-btn
     :browser-node
     (fx/v-box
      (fx/h-box address-bar load-btn)
      wv)}))

(def test-url "http://localhost:10666/")
(def wv (atom nil))
(defn browser-demo []
  (let [;click-ch (chan)
        {:keys [browser-node webview]} (browser :init-url test-url)
       _  (reset! wv webview)
        stack (fx/stack-pane webview #_browser-node)]
    #_browser-node
    stack))

;;(fx/run! (.setZoom @wv 4.0))

(defn web-demo []
  (let [browser (doto (WebBrowserController.)
                      (.createNewTab (let [arr (make-array String 1)] (aset arr  0 "http://localhost:10666/") arr)))
        ]
    #_(fx/stack-pane browser)
    browser))
;; Creates a "sandbox" JavaFX window to
;; show the view. Clicking F5 in this
;; window will refresh the view allowing the
;; create-view function to be updated at the REPL
(defn show-chart []
  (fx/sandbox #'browser-demo)) 

(defn show-browser []
  (fx/sandbox #'web-demo))

(ns core
    (:require
      [componets.todo.todo :as td]
      [reagent.dom :as d]))

;; -------------------------
;; Views

(defn home-page []
  [:div "Todos"
   (td/todo-list)
   (td/todo-form)
   ])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))

(ns componets.todo.todo
  (:require
   [reagent.core :as r]
   [clojure.string :as str]))

(def todos
  (r/atom [{:desc "buy groceries" :status "done"}
           {:desc "check car engine" :status "pending"}
           {:desc "go to post office" :status "pending"}]))

(defn change-status [index]
  (assoc-in @todos [index :status]
            (if (= (:status (get @todos index)) "done") "pending" "done")))

(defn todo-item
  [index item]
  [:li {:class (if (not (= "done" (:status item))) "pending" "done")}
   [:label
    [:input {:type "checkbox"
             :defaultChecked (if (= (:status item) "done") true false)
             :on-change (fn [e] (reset! todos (change-status index)))
             }]
    [:span (:desc item)]]])

(defn todo-list []
  [:ul {:class "todo-list" :style {:list-style "none" :padding "0"}}
   (let [list (map-indexed vector @todos)]
     (for [item list]
       ^{:key (str "todo-" (get item 0))}
       [todo-item (get item 0) (get item 1)]))])

(defn todo-form []
  (let [new-item (r/atom "")]
    [(fn [] [:form {:class "todo-form"
                    :on-submit (fn [e]
                                 (.preventDefault e)
                                 (if-not (str/blank? @new-item)
                                   (swap! todos conj {:desc @new-item :status "pending"}))
                                 (reset! new-item ""))}
             [:h2 {:class "td-form-title"} "New todo"]
             [:div {:class "td-form-input-container"}
              [:input {:type "text"
                       :id "new-item"
                       :value @new-item
                       :placeholder "New todo"
                       :autoFocus true
                       :on-change (fn [e] (reset! new-item (.-value (.-target e))))}]
              [:button {:type "submit"} "Add new"]]])]))

(defn ^:export items []
  [todo-list todo-form])

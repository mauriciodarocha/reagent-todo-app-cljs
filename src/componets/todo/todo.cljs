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

(defn remove-item [index]
    (reset! todos (vec (keep-indexed #(when-not (= index %1) %2) @todos))))

(defn todo-item
  [index item]
  [:li {:class (if (not (= "done" (:status item))) "pending" "done")}
   [:label
    [:input {:type "checkbox"
             :checked (not (= "pending" (:status item)))
             :on-change (fn [e] 
                          (reset! todos (change-status index))
                          )
             }]
    [:span (:desc item)]]
   [:button {:class (str/join " " ["btn-delete" (if (= (:status item) "done") "show" "hide")])
             :on-click (fn [e] (remove-item index))
             }]])

(defn todo-list []
  [:div {:class "todo-list"}
   (if (> (count @todos) 0)
    [:ul {:class "td-list" :style {:list-style "none" :padding "0"}}
     (let [list (map-indexed vector @todos)]
       (for [item list]
         ^{:key (str "todo-" (get item 0))}
         [todo-item (get item 0) (get item 1)]))]
    [:span {:class "empty-list"} "Nothing to do. Add items to your list."])]
  )

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

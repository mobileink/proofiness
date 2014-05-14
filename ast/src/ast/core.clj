(ns ast.core
  ;;(ns clojure.tools.analyzer.core-test
  (:refer-clojure :exclude [macroexpand-1])
  (:require [clojure.tools.analyzer :as ana]
            [clojure.tools.analyzer.passes.emit-form :as ef]
            ;; [clojure.test :refer [deftest is]]
            [clojure.tools.analyzer.ast :as ast]
            [clojure.tools.analyzer.utils :as u]
            [clojure.pprint :as pp]))

(defn desugar-host-expr [[op & expr :as form]]
  (if (symbol? op)
    (let [opname (name op)]
      (cond

       (= (first opname) \.) ; (.foo bar ..)
       (let [[target & args] expr
             args (list* (symbol (subs opname 1)) args)]
         (with-meta (list '. target (if (= 1 (count args)) ;; we don't know if (.foo bar) ia
                                      (first args) args)) ;; a method call or a field access
           (meta form)))

       (= (last opname) \.) ;; (class. ..)
       (with-meta (list* 'new (symbol (subs opname 0 (dec (count opname)))) expr)
         (meta form))

       :else form))
    form))

(defn macroexpand-1 [form env]
  (if (seq? form)
    (let [op (first form)]
      (if (ana/specials op)
        form
        (let [v (u/resolve-var op env)]
          (if (and (not (-> env :locals (get op))) ;; locals cannot be macros
                   (:macro (meta v)))
            (apply v form env (rest form)) ; (m &form &env & args)
            (desugar-host-expr form)))))
    form))

(defmacro foo [] 1)

(def e {:context    :expr
        :locals     {}
        :ns         'user
        :namespaces (atom
                     {'user         {:mappings (into (ns-map 'clojure.core)
                                                     {'foo #'foo})
                                     :aliases  {}
                                     :ns       'user}
                      'clojure.core {:mappings (ns-map 'clojure.core)
                                     :aliases {}
                                     :ns      'clojure.core}})})

(defmacro ast [form]
  `(binding [ana/macroexpand-1 macroexpand-1
             ana/create-var    ~(fn [sym env]
                                  (doto (intern (:ns env) sym)
                                    (reset-meta! (meta sym))))
             ana/parse         ana/-parse
             ana/var?          ~var?]
     (ana/analyze '~form e)))

(defmacro mexpand [form]
  `(macroexpand-1 '~form e))

(def foo-ast (ast ^:foo [1 2]))

(def l-ast (ast (def a  (let [b 1] (+ a b)))))
(l-ast :form)

(defn pre [foo] (print
               (format "%s : %s :: "
                       (foo :type) (foo :op))))

(defn postproc [node]
  (do (print
       (format "%s / %s\n"
               (node :form) (node :children)))

      ))

(defn foo [x] (do (pre x) (postx x)))

(ast/postwalk l-ast postproc)

;; (pp/pprint (first (ast/nodes l-ast)))

;; (pp/pprint (ast/nodes l-ast))

;; (doc ast/prewalk)

;; (ast/postwalk l-ast post)


;; (pp/pprint foo-ast)

;; (:top-level foo-ast) ;; true
;; (:meta foo-ast) ;; a big map {:op :map, :env {...}
;; (:op foo-ast) ;; :with-meta
;; (:form foo-ast) ;; [1 2]

;; (:expr foo-ast)
;; (-> foo-ast :expr  :op) ;; :vector
;; (-> foo-ast :expr  :form) ;; [1 2]
;; (-> foo-ast :expr  :env) ;; a big map - see (:env foo-ast) for content

;; (foo-ast :children) ;; [:meta :expr]

;; (:env foo-ast) ;; a big map
;; (-> foo-ast :env :context) ;; :expr
;; (-> foo-ast :env :locals) ;; {}
;; (-> foo-ast :env :ns) ;; user
;; (-> foo-ast :env :namespaces) ;; atom with big map


;; (-> foo-ast :meta :op)
;; (-> foo-ast :meta :env)
;; (-> foo-ast :meta :keys)
;; (-> foo-ast :meta :vals)


;; (-> foo-ast :meta :form)
;; (-> foo-ast :meta :children)
;; (-> foo-ast :meta :type)
;; (-> foo-ast :meta :literal?)
;; (-> foo-ast :meta :val)
;; (-> foo-ast :meta :form)

;; (pp/pprint foo-ast)

;; (def do-ast (ast ^:frag (do 1 2 3)))
;; (do-ast :top-level) ;; true
;; (do-ast :op) ;; :do
;; (do-ast :meta) ;; nil
;; (do-ast :form)
;; (do-ast :expr)
;; (-> do-ast :statements first :form)
;; (->> do-ast :statements (mapv :form))


;; ;(def cmt-ast (ast ';' comment))


;; (def dom-ast (ast ^{:frag {:id "frag1" :doc "foo doc"}} (do 1 2 3)))

;; (def do3-ast (ast ^:frag (do 1 2 3)))

;; (def x ^:frag (do (println "foo") (println "bar")))

;; (def ^:frag y  3)
;; y
;; (:meta y)
;; (meta x)


;; (defn inc [a] (+ a 1))

;; (inc 3)

;; (def inc-ast (ast (defn inc [a] (+ a 1))))

;; (-> inc-ast :form)
;; (-> inc-ast :op)
;; (-> inc-ast :name)
;; (-> inc-ast :type)

;; (ef/emit-form inc-ast)

;; (-> inc-ast :children)

;; (pp/print inc-ast)


;; (-> inc-ast :init)

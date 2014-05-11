
;; planned syntax:

;; (theory 'foo ;; where foo is a function; or foo/bar :as bar
;;         (theorem (_ "bar") --> 4)  ;; _ means foo
;;         (theorem (_ "baz") --> 5)) ;; ditto

;; (theory 'foo.org :as foo ;; a namespace
;;         (theorem (foo/bar 2) --> 4)
;;         (theorem (foo/baz 3) --> 5))

;; runner actions:

lein proof prove ;; runs tests

lein proof coverage ;; compares theory of namespace to census of namespace funcs

;; something also to check that all multimethods are exercised

lein proof gen ;; read code, generate theory template

lein proof teach ;; print tests for examination (cmp Concordion)

(defmacro theory [& args]
  )

;; parsing borrowed from clojure/core.clj

(defn- load-libs
  "Loads libs, interpreting libspecs, prefix lists, and flags for
forwarding to load-lib"
  [& args]
  (let [flags (filter keyword? args)
        opts (interleave flags (repeat true))
        args (filter (complement keyword?) args)]
    ; check for unsupported options
    (let [supported #{:as :reload :reload-all :require :use :verbose :refer}
          unsupported (seq (remove supported flags))]
      (throw-if unsupported
                (apply str "Unsupported option(s) supplied: "
                     (interpose \, unsupported))))
    ; check a load target was specified
    (throw-if (not (seq args)) "Nothing specified to load")
    (doseq [arg args]
      (if (libspec? arg)
        (apply load-lib nil (prependss arg opts))
        (let [[prefix & args] arg]
          (throw-if (nil? prefix) "prefix cannot be nil")
          (doseq [arg args]
            (apply load-lib prefix (prependss arg opts))))))))


(defn require
  "Loads libs, skipping any that are already loaded. Each argument is
either a libspec that identifies a lib, a prefix list that identifies
multiple libs whose names share a common prefix, or a flag that modifies
how all the identified libs are loaded. Use :require in the ns macro
in preference to calling this directly.

Libs

A 'lib' is a named set of resources in classpath whose contents define a
library of Clojure code. Lib names are symbols and each lib is associated
with a Clojure namespace and a Java package that share its name. A lib's
name also locates its root directory within classpath using Java's
package name to classpath-relative path mapping. All resources in a lib
should be contained in the directory structure under its root directory.
All definitions a lib makes should be in its associated namespace.

'require loads a lib by loading its root resource. The root resource path
is derived from the lib name in the following manner:
Consider a lib named by the symbol 'x.y.z; it has the root directory
<classpath>/x/y/, and its root resource is <classpath>/x/y/z.clj. The root
resource should contain code to create the lib's namespace (usually by using
the ns macro) and load any additional lib resources.

Libspecs

A libspec is a lib name or a vector containing a lib name followed by
options expressed as sequential keywords and arguments.

Recognized options:
:as takes a symbol as its argument and makes that symbol an alias to the
lib's namespace in the current namespace.
:refer takes a list of symbols to refer from the namespace or the :all
keyword to bring in all public vars.

Prefix Lists

It's common for Clojure code to depend on several libs whose names have
the same prefix. When specifying libs, prefix lists can be used to reduce
repetition. A prefix list contains the shared prefix followed by libspecs
with the shared prefix removed from the lib names. After removing the
prefix, the names that remain must not contain any periods.

Flags

A flag is a keyword.
Recognized flags: :reload, :reload-all, :verbose
:reload forces loading of all the identified libs even if they are
already loaded
:reload-all implies :reload and also forces loading of all libs that the
identified libs directly or indirectly load via require or use
:verbose triggers printing information about each load, alias, and refer

Example:

The following would load the libraries clojure.zip and clojure.set
abbreviated as 's'.

(require '(clojure zip [set :as s]))"
  {:added "1.0"}

  [& args]
  (apply load-libs :require args))


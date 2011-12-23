; Stolen from Brian Carper:
;   http://briancarper.net/blog/415/clojure-and-markdown-and-javascript-and-java-and
;
(ns logopoioi.models.markdown
  (:import (org.mozilla.javascript Context ScriptableObject)))

(defn md->html [txt]
  (let [cx (Context/enter)
        scope (.initStandardObjects cx)
        input (Context/javaToJS txt scope)
        script (str (slurp "src/js/showdown.js")
                           "new Showdown.converter().makeHtml(input);")]
    (try
      (ScriptableObject/putProperty scope "input" input)
      (let [result (.evaluateString cx scope script "<cmd>" 1 nil)]
        (Context/toString result))
      (finally (Context/exit)))))

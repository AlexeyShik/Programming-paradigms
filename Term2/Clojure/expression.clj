;HW10
;Base
(def constant constantly)
(defn variable [x] (fn [values] (get values x)))
(defn operation [f] (fn [& arg] (fn [values] (apply f (mapv #(% values) arg)))))
(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def my-div (fn ([x & arg] (reduce #(/ (double %1) (double %2)) x arg))))
(def divide (operation my-div))
(def negate (operation #(- %)))
;Modification
(def med (operation (fn [& arg] (aget (double-array (sort arg)) (quot (count arg) 2)))))
(defn avg-func [& arg] (my-div (apply + arg) (count arg)))
(def avg (operation avg-func))
;Parser
(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate 'med med 'avg avg})
(defn abstractParser [_operations _variable _constant]
  (fn [s]
    (letfn [(parse [expr]
              (cond
                (seq? expr) (apply (get _operations (first expr)) (mapv parse (rest expr)))
                (number? expr) (_constant expr)
                :else (_variable (str expr))))]
      (parse (read-string s)))))
(def parseFunction (abstractParser operations variable constant))

;HW11
;Object
(definterface IOperation
  (^Number evaluate [vars])
  (^String toString [])
  (^String toStringInfix [])
  (diff [t]))

(def evaluate #(.evaluate %1 %2))
(def toString #(.toString %))
(def toStringInfix #(.toStringInfix %))
(def diff #(.diff %1 %2))
(defn diff-all [args t] (mapv #(diff % t) args))

(deftype _JConstAndVariable [evaluate-func toString-func diff-func value]
  IOperation
  (evaluate [this vars] (evaluate-func vars value))
  (toString [this] (toString-func value))
  (toStringInfix [this] (toString-func value))
  (diff [this t] (diff-func value t)))

(def JConstAndVariable #(_JConstAndVariable. %1 %2 %3 %4))
(declare ZERO)
(def Constant (partial JConstAndVariable #(identity %2) #(format "%.1f" (double %)) (fn [this t] ZERO)))
(def ZERO (Constant 0.0))
(def ONE (Constant 1.0))
(def Variable (partial JConstAndVariable #(get %1 %2) identity #(if (= %1 %2) ONE ZERO)))

(deftype _JOperation [symbol f calc-diff args]
  IOperation
  (evaluate [this vars] (apply f (map #(evaluate % vars) args)))
  (toString [this] (str "(" symbol " " (clojure.string/join " " (map #(toString %) args)) ")"))
  (toStringInfix [this] (if (== 1 (count args))
                          (str symbol "(" (toStringInfix (first args)) ")")
                          (let [rev-args (reverse args)]
                            (reduce
                              #(str "(" (toStringInfix %2) " " symbol " " %1 ")")
                              (toStringInfix (first rev-args))
                              (rest rev-args)))))
  (diff [this t] (calc-diff (vec args) (diff-all args t))))
(def JOperation #(_JOperation. %1 %2 %3 %&))
(def construct-operation #(partial JOperation %1 %2 %3))
(def construct-easy-diff #(construct-operation %1 %2 (fn [args diff-args] (apply (force %3) diff-args))))

(def Add (construct-easy-diff '+ + (delay Add)))
(def Subtract (construct-easy-diff '- - (delay Subtract)))
(def Multiply (construct-operation '* *
                                   #(apply Add (mapv
                                                 (fn [k] (apply Multiply (assoc %1 k (get %2 k))))
                                                 (range (count %1))))))
(def Divide (construct-operation '/ my-div
                                 #(let [args-rest (apply Multiply (rest %1))]
                                    (Divide
                                      (Subtract
                                        (Multiply (first %2) args-rest)
                                        (Multiply (first %1) (apply Multiply (rest %2))))
                                      (Multiply args-rest args-rest)))))
(def Negate (construct-easy-diff 'negate #(- %) (delay Negate)))
;hw11 modification
(def Sum (construct-easy-diff 'sum + (delay Add)))
(def Avg (construct-operation 'avg avg-func #(Divide (apply Add %2) (Constant (count %1)))))
;hw12 modification
(defn right-assoc-func [f]
  (fn [& args]
    (let [rev-args (reverse args)]
      (reduce #(f %2 %1) (first rev-args) (rest rev-args)))))
(def ln #(Math/log (Math/abs %)))
(def Pow (construct-easy-diff '** (right-assoc-func #(Math/pow %1 %2)) (delay Pow)))
(def Log (construct-easy-diff "//" (right-assoc-func #(if (== %1 0.0) 0.0 (my-div (ln %2) (ln %1)))) (delay Log)))

;Prefix parser
(def Operations {'+ Add '- Subtract '** Pow (symbol "//") Log '* Multiply '/ Divide 'negate Negate 'sum Sum 'avg Avg})
(def parseObject (abstractParser Operations Variable Constant))

;Combinator parser library
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (println)
  (run! (fn [input] (printf "    %-10s %s\n" input (_show (parser input)))) inputs))

(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f]
  (fn [result]
    (if (-valid? result)
      (-return (f (-value result)) (-tail result)))))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar)))
         ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (_map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce (partial _either) p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec []
            (+or
              (+seqf cons p (delay (rec)))
              (_empty ())))]
    (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

;Infix parser
;Review HW12
(def *digit (+char "0123456789"))
(def *number
  (+seqf
    #(str %1 (apply str %2) %3 (apply str %4))
    (+opt (+char "+-"))
    (+plus *digit)
    (+opt (+char "."))
    (+opt (+plus *digit))))
(def *wrapped-whitespaces #(+seqn 0 *ws % *ws))
(defn func-for-*str [f x] (mapv #(f (str %)) x))
(def *str #(apply +seqf str (func-for-*str +char %)))
(def *str-from #(apply +or (func-for-*str *str %)))

(def build-operation (fn [operation & operands] (apply (get Operations (symbol operation)) operands)))
(def construct-left-associative
  (fn [[a & [args]]]
    (reduce (fn [a [symbol b]] (build-operation symbol a b)) a args)))
(def construct-right-associative
  (fn [[a & [args]]]
    (if (empty? args) a (let [fi (first args)]
                          (build-operation (first fi) a (construct-right-associative (vector (second fi) (rest args))))))))

(declare *infix-level-3)
(def *infix-value
  (*wrapped-whitespaces
    (+or
      (+map #(Constant (read-string %)) *number)
      (+map Variable (*str-from ['x 'y 'z]))
      (+map #(build-operation (first %) (second %)) (+seq (*str-from ['negate]) *ws (delay *infix-value)))
      (+seqn 1 (+char "(") (delay *infix-level-3) (+char ")")))))

(defn *infix-nth-level [constructor prev-level symbol-parser]
  (+map constructor (+seq prev-level (+star (+seq (*wrapped-whitespaces symbol-parser) prev-level)))))

(def left-associative (partial *infix-nth-level construct-left-associative))
(def right-associative (partial *infix-nth-level construct-right-associative))

(def *infix-level-1 (right-associative *infix-value (*str-from ['** (symbol "//")])))
(def *infix-level-2 (left-associative *infix-level-1 (*str-from ['* '/])))
(def *infix-level-3 (left-associative *infix-level-2 (*str-from ['+ '-])))

(def parseObjectInfix (+parser *infix-level-3))
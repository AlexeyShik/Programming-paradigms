;Common
(defn check-nil [& arg] (and (fn [x] (not (nil? x))) arg))

(defn same-size? [& arg]
  (let [c (count (first arg))]
    (every? (fn [v] (== c (count v))) arg)))

(defn something-of? [check-type]
  {:pre [(check-nil check-type)]
   :post [(check-nil %)]}
  (fn [& arg] (and (every? (and vector? check-type) arg) (apply == (mapv count arg)))))

(defn calc [func & arg]
  {:pre [(or (every? number? arg) (and (every? vector? arg) (apply == (mapv count arg))))]
   :post [(check-nil %)]}
  (cond
    (every? number? arg)
    (apply func arg)
    :else
    (apply mapv (partial calc func) arg)))

(defn element-operation [func check-type]
  {:pre [(check-nil func check-type)]
   :post [(check-nil %)]}
  (letfn
    [(check [& arg]
     {:pre [(check-nil func check-type) (apply check-type arg)]
      :post [(check-type %)]}
     (apply calc func arg))]
    :return check))

(defn make-element-operation [check-type]
  {:pre [(check-nil check-type)]
   :post [(check-nil %)]}
  (fn [f]
    {:pre [(check-nil f)]
     :post [(check-nil %)]}
    (element-operation f check-type)))

(defn reducer [func check-type check-extra]
  {:pre  [(check-nil func check-type check-extra)]
   :post [(check-nil %)]}
  (fn [x & arg]
    {:pre  [(check-type x) (every? check-type arg) (check-extra x arg)]
     :post [(check-nil %)]}
    (reduce func x arg)))

(defn scalar-multiply [func check-type]
  {:pre [(check-nil func check-type)]
   :post [(check-nil %)]}
  (fn [x & arg]
  {:pre [(check-type x) (every? number? arg)]
  :post [(check-type %)]}
  (let [c (apply * arg)]
    (mapv (fn [v] (func v c)) x))))

;Vectors
(def vector-of-number? (something-of? (fn [v] (every? number? v))))
(def vector-element-operation (make-element-operation vector-of-number?))
(def v+ (vector-element-operation +))
(def v- (vector-element-operation -))
(def v* (vector-element-operation *))
(def v*s (scalar-multiply * vector-of-number?))

(defn scalar [u v]
  {:pre  [(vector-of-number? u) (vector-of-number? v) (same-size? u v)]
   :post [(number? %)]}
  (reduce + (v* u v)))

(def vect
  (reducer
    (fn [u v]
      {:pre [(vector-of-number? u v)]
       :post [(vector-of-number? %)]}
      (letfn [(diag [i j] (* (get u i) (get v j)))
              (coord [i j] (- (diag i j) (diag j i)))]
        (vector (coord 1 2) (- (coord 0 2)) (coord 0 1))))
    vector-of-number? (fn [x arg] (and (== (count x) 3) (same-size? (concat x arg))))))

;Matrixes
(def matrix-of-number? (something-of? (fn [x] (apply vector-of-number? x))))
(def matrix-element-operation (make-element-operation matrix-of-number?))
(def m+ (matrix-element-operation +))
(def m- (matrix-element-operation -))
(def m* (matrix-element-operation *))
(def m*s (scalar-multiply v*s matrix-of-number?))

(defn transpose [m]
  {:pre  [(matrix-of-number? m)]
   :post [(matrix-of-number? %)]}
  (apply mapv vector m))

(defn m*v [m v]
  {:pre  [(matrix-of-number? m) (vector-of-number? v) (same-size? (transpose m) v)]
   :post [(vector-of-number? %)]}
  (mapv (partial scalar v) m))

(def m*m
  (reducer
    (fn [a b]
      {:pre [(matrix-of-number? a) (matrix-of-number? b) (same-size? (transpose a) b)]
       :post [(matrix-of-number? %)]}
      (mapv (partial m*v (transpose b)) a))
    matrix-of-number? (constantly true)))

;Tensor
(defn tensor-of-number? [& arg] (or (every? number? arg) (and (every? vector? arg) (apply == (mapv count arg)) (apply tensor-of-number? (apply concat [] arg)))))
(def tensor-element-operation (make-element-operation tensor-of-number?))
(def t+ (tensor-element-operation +))
(def t- (tensor-element-operation -))
(def t* (tensor-element-operation *))

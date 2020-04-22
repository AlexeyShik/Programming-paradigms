;Contract
(defn same-size? [check-size] (fn [arg] (every? (partial check-size (first arg)) arg)))
(def same-size-2-vectors? (fn [u v] (== (count u) (count v))))
(def same-size-vectors? (same-size? same-size-2-vectors?))
(defn same-size-2-matrixes? [u v] (and (same-size-2-vectors? u v) (same-size-2-vectors? (first u) (first v))))
(def same-size-matrixes? (same-size? same-size-2-matrixes?))
(defn vector-of? [v check-type] (and (vector? v) (every? check-type  v)))
(defn vector-of-number? [v] (vector-of? v number?))
(defn matrix-of? [m check-type] (and (vector-of? m check-type) (same-size-vectors? m)))
(defn matrix-of-number? [m] (matrix-of? m vector-of-number?))

;Common
(defn reducer [func check-type check-every check-extra]
  {:pre [(not (nil? func)) (not (nil? check-type)) (not (nil? check-every)) (not (nil? check-extra))]
   :post [(not (nil? %))]}
  (fn [x & arg]
    {:pre [(check-type x) (every? check-every arg) (check-extra x arg)]
     :post [(not (nil? %))]}
    (reduce func x arg)))

(defn element-operation [check-every check-size]
  {:pre [(not (nil? check-every)) (not (nil? check-size))]
   :post [(not (nil? %))]}
  (fn [func]
    {:pre [(not (nil? func))]
     :post [(not (nil? %))]}
    (fn [& arg]
      {:pre [(every? check-every arg) (check-size arg)]
       :post [(check-every %)]}
      (apply mapv func arg))))

;Vectors
(def vector-element-operation (element-operation vector-of-number? same-size-vectors?))
(def v+ (vector-element-operation +))
(def v- (vector-element-operation -))
(def v* (vector-element-operation *))
(def v*s (reducer (fn [v s] (mapv (partial * s) v)) vector-of-number? number? (constantly true)))
(defn scalar [u v]
  {:pre [(vector-of-number? u) (vector-of-number? v) (same-size-2-vectors? u v)]
   :post [(number? %)]}
  (reduce + (v* u v)))
(def vect (reducer (fn [u v] (letfn [(coord [i j] (- (* (get u i) (get v j)) (* (get u j) (get v i))))] (vector (coord 1 2) (- (coord 0 2)) (coord 0 1))))
            vector-of-number? vector-of-number? (fn [x args] (and (or (== 0 (count args)) (same-size-2-vectors? x (first args))) (same-size-vectors? args)))))

;Matrixes
(def matrix-element-operation (element-operation matrix-of-number? same-size-matrixes?))
(def m+ (matrix-element-operation v+))
(def m- (matrix-element-operation v-))
(def m* (matrix-element-operation v*))
(defn transpose [m]
  {:pre [(matrix-of-number? m)]
   :post [(matrix-of-number? %)]}
  (apply mapv vector m))
(def m*s (reducer (fn [m s] (mapv (fn [v] (v*s v s)) m)) matrix-of-number? number? (constantly true)))
(defn m*v [m v]
  {:pre [(matrix-of-number? m) (vector-of-number? v) (same-size-2-vectors? (transpose m) v)]
   :post [(vector-of-number? %)]}
  (mapv (partial scalar v) m))
(def m*m (reducer (fn [a b] (mapv (partial m*v (transpose b)) a)) matrix-of-number? matrix-of-number? (constantly true)))

(ns logopoioi.models.logos
  (:use [clojure.string :only [split]])
  (:import [org.apache.hadoop.hbase.client HTable Put Get Delete Scan] 
           [org.apache.hadoop.hbase.util Bytes] 
           [org.apache.hadoop.hbase HBaseConfiguration]))


(def TABLE-NAME "logopoioi")
(def COLUMN-FAM "logos")
(def COUNTER-ROW "0")
(def LOG-COL "log")
(def COUNTER-COL "counter")

(defn- get-table []
  (let [config (HBaseConfiguration/create)
        table (HTable. config TABLE-NAME)]
    (.setAutoFlush table false);
    (.setWriteBufferSize table (* 1024 1024 12))
    table))

(defn- get-col [id col]
 (let [table (get-table)
        get-obj (Get. (Bytes/toBytes id)) ]
    (.addColumn get-obj (Bytes/toBytes COLUMN-FAM) (Bytes/toBytes col))
    (let [result (.get table get-obj)]
      (.close table)
      (Bytes/toString (.getValue (.get (.list result) 0))))))

(defn- put [row-id col value]
  (let [table (get-table)
        put-obj (Put. (Bytes/toBytes row-id))]
    (.add put-obj (Bytes/toBytes COLUMN-FAM) 
          (Bytes/toBytes col) (Bytes/toBytes value))
    (.put table put-obj)
    (.close table)))

(defn- all-rows []
  (let [table (get-table)
        scan-obj (Scan.)
        result-scanner (.getScanner table scan-obj)]
    (.close table)
    (seq result-scanner)))

(defn- del-row [id]
  (let [table (get-table)
        del-obj (Delete. (Bytes/toBytes id))]
    (.delete table del-obj)
    (.close table)))

;TODO -> move above to a hbase module
;-------------------------------------------------
(defprotocol Note 
  (content [self] )
  (title [self])
  (identifier [self]))

(defn make-note [id text]
  (reify Note
    (content [_] text)
    (title [_] ((split text #"\n") 0))
    (identifier [_] id)))

(defn- next-log-row []
  (let [ctr (Integer/parseInt (get-col COUNTER-ROW COUNTER-COL))]  
    (put "0" COUNTER-COL (str (inc ctr)))
    (str ctr)))

(defn- result->row-id [result]
  (Bytes/toString (.getRow result)))

(defn- filter-out-counter [results]
  (filter #(not (= "0" (result->row-id %))) results))
  
(defn- result->note [result]
  (let [id (result->row-id result)
        text (Bytes/toString (.getValue result 
                        (Bytes/toBytes COLUMN-FAM) 
                        (Bytes/toBytes LOG-COL)))]
    (make-note id text)))

(defn all-notes []
  (->> (all-rows)
       (filter-out-counter)
       (map result->note)))

(defn delete [note]
  (del-row (identifier note)))

(defn fetch [id]
  (make-note id (get-col id LOG-COL)))

(defn update [note]
  (put (identifier note) LOG-COL (content note))
  note)

(defn create [text]
  (let [id (next-log-row)
        note (make-note id text)]
    (update note)
    note))

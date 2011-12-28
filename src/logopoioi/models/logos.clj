(ns logopoioi.models.logos
  (:use [clojure.string :only [split]])
  (:require [logopoioi.models.hbase :as hbase])
  (:import [org.apache.hadoop.hbase.util Bytes]))


(def COUNTER-ROW "0")
(def LOG-COL "log")
(def COUNTER-COL "counter")

(defprotocol Note 
  (content [self] )
  (title [self])
  (bangtags [self])
  (identifier [self]))

(defn text->!tags [text]
  (re-seq #"!\w*" text))

(defn make-note [id text]
  (reify Note
    (content [_] text)
    (title [_] ((split text #"\n") 0))
    (bangtags [_] (text->!tags text))
    (identifier [_] id)))

(defn- next-log-row []
  (let [ctr (Integer/parseInt (hbase/get-col COUNTER-ROW COUNTER-COL))]  
    (hbase/put "0" COUNTER-COL (str (inc ctr)))
    (str ctr)))

(defn- result->row-id [result]
  (Bytes/toString (.getRow result)))

(defn- filter-out-counter [results]
  (filter #(not (= "0" (result->row-id %))) results))
  
(defn- result->note [result]
  (let [id (result->row-id result)
        text (Bytes/toString (.getValue result 
                        (Bytes/toBytes hbase/COLUMN-FAM) 
                        (Bytes/toBytes LOG-COL)))]
    (make-note id text)))

(defn all-notes []
  (->> (hbase/all-rows)
       (filter-out-counter)
       (map result->note)))

(defn delete [id]
  (hbase/del-row id))

(defn fetch [id]
  (make-note id (hbase/get-col id LOG-COL)))

(defn update [note]
  (hbase/put (identifier note) LOG-COL (content note))
  note)

(defn create [text]
  (let [id (next-log-row)
        note (make-note id text)]
    (update note)
    note))

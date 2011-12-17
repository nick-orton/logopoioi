(ns logopoioi.models.logos
  (:import [org.apache.hadoop.hbase.client HTable Put Get] 
           [org.apache.hadoop.hbase.util Bytes] 
           [org.apache.hadoop.hbase HBaseConfiguration]))


(def TABLE-NAME "test")
(def COLUMN-FAM "cf")
(def LOG-COL "a")


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

(defn fetch [id]
  (get-col id LOG-COL))

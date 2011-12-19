(ns logopoioi.models.hbase
  (:import [org.apache.hadoop.hbase.client HTable Put Get Delete Scan] 
           [org.apache.hadoop.hbase.util Bytes] 
           [org.apache.hadoop.hbase HBaseConfiguration]))

(def TABLE-NAME "logopoioi")
(def COLUMN-FAM "logos")

(defn- get-table []
  (let [config (HBaseConfiguration/create)
        table (HTable. config TABLE-NAME)]
    (.setAutoFlush table false);
    (.setWriteBufferSize table (* 1024 1024 12))
    table))

(defn get-col [id col]
 (let [table (get-table)
        get-obj (Get. (Bytes/toBytes id)) ]
    (.addColumn get-obj (Bytes/toBytes COLUMN-FAM) (Bytes/toBytes col))
    (let [result (.get table get-obj)]
      (.close table)
      (Bytes/toString (.getValue (.get (.list result) 0))))))

(defn put [row-id col value]
  (let [table (get-table)
        put-obj (Put. (Bytes/toBytes row-id))]
    (.add put-obj (Bytes/toBytes COLUMN-FAM) 
          (Bytes/toBytes col) (Bytes/toBytes value))
    (.put table put-obj)
    (.close table)))

(defn all-rows []
  (let [table (get-table)
        scan-obj (Scan.)
        result-scanner (.getScanner table scan-obj)]
    (.close table)
    (seq result-scanner)))

(defn del-row [id]
  (let [table (get-table)
        del-obj (Delete. (Bytes/toBytes id))]
    (.delete table del-obj)
    (.close table)))



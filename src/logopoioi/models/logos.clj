(ns logopoioi.models.logos
  (:import [org.apache.hadoop.hbase.client HTable Put Get] 
           [org.apache.hadoop.hbase.util Bytes] 
           [org.apache.hadoop.hbase HBaseConfiguration]
)
  )

(defn get-table []
  (let [config (HBaseConfiguration/create)
        table (HTable. config "test")]
    (.setAutoFlush table false);
    (.setWriteBufferSize table (* 1024 1024 12))
    table
  ))

(defn fetch [id]
  (let [table (get-table)
        rowId (Bytes/toBytes id)
        get-obj (Get. rowId)
        ]
    (.addColumn get-obj (Bytes/toBytes "cf") (Bytes/toBytes "a"))
    (let [result (.get table get-obj)]
      (.close table)
      (Bytes/toString (.getValue (.get (.list result) 0))))))

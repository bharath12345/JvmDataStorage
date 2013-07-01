package com.appnomic.appsone.cassandra.utility;

/**
 * User: bharadwaj
 * Date: 01/07/13
 * Time: 3:38 PM
 */
public class Constants {

    // keyspace names
    public enum Keyspaces {
        JvmMethodMetrics;

        public String toString() {
            switch (this) {
                case JvmMethodMetrics:
                    return "JvmMethodMetrics";
            }
            return null;
        }

    }

    // table names or column families
    public enum ColumnFamilies {
        JvmMethodMetricsRaw,
        JvmMethodIdNameMap;

        public String toString() {
            switch (this) {
                case JvmMethodMetricsRaw:
                    return "JvmMethodMetricsRaw";
                case JvmMethodIdNameMap:
                    return "JvmMethodIdNameMap";
            }
            return null;
        }

    }

    public enum JvmMethodIdNameMap {
        jvm_id,
        method_id,
        method_name;

        public String toString() {
            switch (this) {
                case jvm_id:
                    return "jvm_id";
                case method_id:
                    return "method_id";
                case method_name:
                    return "method_name";
            }
            return null;
        }
    }

    public enum JvmMethodMetricsRaw {
        jvm_id,
        date,
        day_time,
        method_id,
        invocations,
        response_time;

        public String toString() {
            switch(this) {
                case jvm_id:
                    return "jvm_id";
                case date:
                    return "date";
                case day_time:
                    return "day_time";
                case method_id:
                    return "method_id";
                case invocations:
                    return "invocations";
                case response_time:
                    return "response_time";
            }
            return null;
        }
    }

}

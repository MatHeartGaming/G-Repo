package org.cis.modello;

import java.util.Comparator;

public class Sorter {

    public class SortByStars implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int val1 = Integer.valueOf(o1.toString());
            int val2 = Integer.valueOf(o2.toString());
            return val1 - val2;
        }
    }

    public class SortByDimension implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            Long val1 = Long.valueOf(o1.trim());
            Long val2 = Long.valueOf(o2.trim());
            if(val1 - val2 > 0) {
                return 1;
            } else if(val1 - val2 == 0) {
                return 0;
            }
            return -1;
        }
    }


}

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

    public class SortByProgrLanguage implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int indexPercent1 = o1.indexOf("%");
            String percent1 = o1.substring(0, indexPercent1);
            int indexPercent2 = o2.indexOf("%");
            String percent2 = o2.substring(0, indexPercent2);
            double percentDouble1 = Double.parseDouble(percent1);
            double percentDouble2 = Double.parseDouble(percent2);
            return Double.compare(percentDouble1, percentDouble2);
        }
    }


}

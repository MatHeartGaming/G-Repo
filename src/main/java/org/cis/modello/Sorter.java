package org.cis.modello;

import org.cis.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

public class Sorter {

    public class SortByStars implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int val1 = Integer.valueOf(o1);
            int val2 = Integer.valueOf(o2);
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

    public class SortByProgrammingLanguage implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            Integer x = compareStringMessage(o1, o2);
            if (x != null) return x;

            int indexPercent1 = o1.indexOf("%");
            String percent1 = o1.substring(0, indexPercent1);
            int indexPercent2 = o2.indexOf("%");
            String percent2 = o2.substring(0, indexPercent2);
            double percentDouble1 = Double.parseDouble(percent1);
            double percentDouble2 = Double.parseDouble(percent2);
            return Double.compare(percentDouble1, percentDouble2);
        }
    }

    public class SortByLastCommitDate implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            Integer x = compareStringMessage(o1, o2);
            if (x != null) return x;

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            LocalDate date1 = LocalDate.parse(o1, dtf);
            LocalDate date2 = LocalDate.parse(o2, dtf);
            return date1.compareTo(date2);
        }
    }

    private Integer compareStringMessage(String o1, String o2) {
        if (o1.equals(Constants.MESSAGE_NOT_DETERMINED_YET) || o2.equals(Constants.MESSAGE_NOT_DETERMINED_YET)) {
            return -1;
        }

        if(o1.equals(Constants.MESSAGE_NOT_EXISTS) && !o2.equals(Constants.MESSAGE_NOT_EXISTS)) {
            return 1;
        }

        if(o1.equals(Constants.MESSAGE_NOT_EXISTS)) {
            return 0;
        }
        if(o2.equals(Constants.MESSAGE_NOT_EXISTS)) {
            return -1;
        }
        return null;
    }


}

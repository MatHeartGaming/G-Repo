package org.cis.modello;

public class RepositoryLanguage {

    private String language;
    private Detection detection1;
    private Detection detection2;

    public RepositoryLanguage setLanguage(String language) {
        this.language = language;
        return this;
    }

    public RepositoryLanguage setDetection1(String code, double percentage) {
        this.detection1 = new Detection(code, percentage);
        return this;
    }

    public RepositoryLanguage setDetection2(String code, double percentage) {
        this.detection2 = new Detection(code, percentage);
        return this;
    }

    @Override
    public String toString() {
        return this.language + " (" + this.detection1.toString()
                + (this.detection2 == null ? ")" : ", " + this.detection2.toString() + ")");
    }

    public static class Detection {

        private String code;
        private double percentage;

        public Detection(String code, double percentage) {
            this.code = code;
            this.percentage = percentage;
        }

        public String getCode() {
            return code;
        }

        public double getPercentage() {
            return percentage;
        }

        @Override
        public String toString() {
            return this.getCode() + ": " + this.getPercentage() + "%";
        }
    }
}

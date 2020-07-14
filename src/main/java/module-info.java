module org.cis {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.eclipse.jgit;
    requires detectlanguage;

    opens org.cis to javafx.fxml;
    exports org.cis;
}
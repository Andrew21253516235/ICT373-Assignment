module au.edu.murdoch.ict373_assignment2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens au.edu.murdoch.ict373_assignment2 to javafx.fxml;
    exports au.edu.murdoch.ict373_assignment2;
    exports question2_assignment1;
}

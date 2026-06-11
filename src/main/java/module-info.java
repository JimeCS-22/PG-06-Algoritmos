module ucr.algoritmos.pg06algoritmos {
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;


    opens ucr.algoritmos.pg06algoritmos to javafx.fxml;
    opens ucr.algoritmos.pg06algoritmos.model to javafx.base;
    exports ucr.algoritmos.pg06algoritmos;
    exports ucr.algoritmos.pg06algoritmos.controller;
    opens ucr.algoritmos.pg06algoritmos.controller to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    opens ucr.algoritmos.pg06algoritmos.model.linkedList to javafx.base;
    opens ucr.algoritmos.pg06algoritmos.model.stack to javafx.base;
    opens ucr.algoritmos.pg06algoritmos.model.Queue to javafx.base;
    opens ucr.algoritmos.pg06algoritmos.model.Tree to javafx.base;
}
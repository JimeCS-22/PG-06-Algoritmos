module ucr.algoritmos.pg05algoritmos {
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;


    opens ucr.algoritmos.pg05algoritmos to javafx.fxml;
    opens ucr.algoritmos.pg05algoritmos.model to javafx.base;
    exports ucr.algoritmos.pg05algoritmos;
    exports ucr.algoritmos.pg05algoritmos.controller;
    opens ucr.algoritmos.pg05algoritmos.controller to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    opens ucr.algoritmos.pg05algoritmos.model.linkedList to javafx.base;
    opens ucr.algoritmos.pg05algoritmos.model.stack to javafx.base;
    opens ucr.algoritmos.pg05algoritmos.model.Queue to javafx.base;
    opens ucr.algoritmos.pg05algoritmos.model.Tree to javafx.base;
}
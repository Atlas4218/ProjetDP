package appli;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MainController {
    public Label HelloWorld;

    public void sayHelloWorld(ActionEvent actionEvent) throws FileNotFoundException {


        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        // process the file, and limit periods to given time interval
    	genHTML(selectedFile, "CM Bases de données", "19/01/2021", "19/01/2021 à 10:15:00", "19/01/2021 à 11:45:00");

        
    }
    @FXML
    void handleDragOver(DragEvent event){
    	System.out.println("Draged over");
    	if (event.getDragboard().hasFiles()) {
        	event.acceptTransferModes(TransferMode.ANY);

		}
    }
    @FXML
    private void handleDragDrop(DragEvent event) throws FileNotFoundException {
		// TODO Auto-generated method stub
    	System.out.println("Dropped");
    	File selectedFile = event.getDragboard().getFiles().get(0);
    	genHTML(selectedFile, "CM Bases de données", "19/01/2021", "19/01/2021 à 10:15:00", "19/01/2021 à 11:45:00");
	}
    
    
    void genHTML(File file, String intitule, String date, String debut, String fin) throws FileNotFoundException {
    	
    	var teamsProcessor = new TEAMSProcessor(file, intitule, date, debut, fin);
    	
    	/*
        var allpeople = teamsProcessor.get_allpeople();
        for (People people : allpeople) {
            System.out.println( people );
        }
*/
        PrintWriter printWriter = new PrintWriter("result.html");
        printWriter.print(teamsProcessor.toHTMLCode());
        printWriter.close();
        System.out.println( teamsProcessor.toHTMLCode() );
    }
    
}

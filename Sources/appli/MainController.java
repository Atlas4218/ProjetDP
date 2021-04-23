package appli;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class MainController {
	public CheckBox withoutId;
	public CheckBox withoutName;
	public CheckBox withoutPlanning;
	
    public Label HelloWorld;
    public Label fileName;
    public Label date;
    public Label minTime;
    public Label maxTime;
    public Label uploadLabel;
    
    public ToggleGroup sortGroup;
    public RadioButton idSort;
    public RadioButton nameSort;
    public RadioButton timeSort; 
    
    public TextField intituleField;
    public TextField startCourseField;
    public TextField endCourseField;

    File selectedFile = null;
    
    public void validation(ActionEvent actionEvent) throws FileNotFoundException {
       if(selectedFile!=null) {
    	   
    	   if (!(intituleField.getText().isEmpty()
    			   ||startCourseField.getText().isEmpty()
    			   ||endCourseField.getText().isEmpty())) {
    	    	//genHTML(selectedFile, "CM Bases de données", "19/01/2021", "19/01/2021 à 10:15:00", "19/01/2021 à 11:45:00");
    		   genHTML(selectedFile, intituleField.getText(), "19/01/2021", startCourseField.getText(), endCourseField.getText());
		}else
			System.err.println("Manque d'information");
       }
        
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
    	selectedFile = event.getDragboard().getFiles().get(0);
    	fileName.setText(selectedFile.getName());
    	//uploadLabel.setText("Fichier importé");
    	
	}
    @FXML
    private void fileChoose(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
    	FileChooser fileChooser = new FileChooser();
    	selectedFile = fileChooser.showOpenDialog(null);
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

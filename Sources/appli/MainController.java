package appli;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


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
    
    public VBox infoCompletion;

    File selectedFile = null;
    
    public void validation(ActionEvent actionEvent) throws FileNotFoundException {
       if(selectedFile!=null) {
    	   
    	   if (!(intituleField.getText().isEmpty()
    			   ||startCourseField.getText().isEmpty()
    			   ||endCourseField.getText().isEmpty())) {
    	    	//genHTML(selectedFile, "CM Bases de données", "19/01/2021", "19/01/2021 à 10:15:00", "19/01/2021 à 11:45:00");
    		   
    		   
    		   
    		   genHTML(selectedFile, intituleField.getText(), date.getText(), date.getText()+" à "+startCourseField.getText(), date.getText()+" à "+endCourseField.getText());
		}else
			System.err.println("Manque d'information");
       }
       else
    	   System.err.println("Aucun Fichier");
        
    }
    @FXML
    void handleDragOver(DragEvent event){
    	System.out.println("Draged over");
    	if (event.getDragboard().hasFiles()) 
			event.acceptTransferModes(TransferMode.ANY);
    }
    @FXML
    private void handleDragDrop(DragEvent event) throws FileNotFoundException {
		// TODO Auto-generated method stub
    	System.out.println("Dropped");
    	selectedFile = event.getDragboard().getFiles().get(0);
    	//extractions des données du fichier
    	fileName.setText(selectedFile.getName()); //nom du fichier
    	
    	//listes des evenements
    	List<String> times = new ArrayList<String>();
    	var teamsFile = new TEAMSAttendanceList(selectedFile);
        var lines = teamsFile.get_attlist();
        if (lines != null) {
            Iterator<String> element = lines.iterator();
            // Première ligne
            element.next();
            
            while (element.hasNext()) {
                String input = element.next();
                String[] infos = input.split("\t");
                if (infos.length==3) {
                    String instant = infos[2]; //Date et Heure de l'evenement
                    times.add(instant);
                }
            }
        }
        
        Collections.sort(times);
        date.setText(times.get(0).split(" à ")[0]); //Dates
        minTime.setText(times.get(0).split(" à ")[1]); //Heure début
    	maxTime.setText(times.get(times.size()-1).split(" à ")[1]); //Heure fin
    	infoCompletion.setDisable(false);
    	
    	/*intituleField.setText(selectedFile.getName().split(".")[0]); 
    	uploadLabel.setText("Fichier importé");*/
    	
	}
    @FXML
    private void fileChoose(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
    	FileChooser fileChooser = new FileChooser();
    	File file = fileChooser.showOpenDialog(null);
    	if (file == null)
    		selectedFile = file;
	}
    
    
    
    void genHTML(File file, String intitule, String date, String debut, String fin) throws FileNotFoundException {
    	
    	boolean name = !withoutName.isSelected();
    	boolean id = !withoutId.isSelected();
    	boolean planning = !withoutPlanning.isSelected();
    	String comparator = "time";

    	if (idSort.isSelected()) 
			comparator = "id";
		if (nameSort.isSelected())
			comparator = "name";
    	
    	var teamsProcessor = new TEAMSProcessor(file, intitule, date, debut, fin, comparator);
    	
    	/*
        var allpeople = teamsProcessor.get_allpeople();
        for (People people : allpeople) {
            System.out.println( people );
        }*/

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Sauvegarde");
        chooser.setInitialFileName("result.html");
        chooser.getExtensionFilters().add(new ExtensionFilter("Fichier HTML", ".html"));
        File save = chooser.showSaveDialog(null);
        if(save != null) {
        	PrintWriter printWriter = new PrintWriter(save);
        	printWriter.print(teamsProcessor.toHTMLCode(name, id, planning));
        	printWriter.close();
        }
    	
    	
        //System.out.println( teamsProcessor.toHTMLCode(name, id, planning));
    }
    
}

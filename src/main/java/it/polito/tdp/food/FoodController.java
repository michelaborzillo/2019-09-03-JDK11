/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Portion;
import it.polito.tdp.food.model.Vertici;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<Vertici> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    Vertici porzione= boxPorzioni.getValue();
    
    if (porzione==null) {
		txtResult.appendText("ERRORE: devi selezionare una porzione\n");
	return ;
	}
	Integer N ;
	try {
		N = Integer.parseInt(txtPassi.getText()) ;
	} catch(NumberFormatException ex) {
		txtResult.appendText("ERRORE: il valore "+txtPassi.getText()+" non è un numero intero\n");
		return ;
	}
	model.cercaLista(porzione, N);
	
	if(model.best==null) {
		txtResult.appendText("Non ho trovato un cammino di lunghezza N\n");
	} else {
		txtResult.appendText("Trovato un cammino di peso "+model.max+"\n");
		for(Vertici vertice : model.best) {
			txtResult.appendText(vertice+"\n");
		}
	}
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	Vertici v= boxPorzioni.getValue();
    	model.getAdiacenza(v);
    	for (Adiacenza po: model.getAdiacenza(v)) {
    		txtResult.setText(po+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String calor=txtCalorie.getText();
    	try {
    		int calorie= Integer.parseInt(calor);
    		model.creaGrafo(calorie);
    		txtResult.appendText("Grafo creato!\n");
    		txtResult.appendText("VERTICI: "+this.model.nVertici()+"\n");
        	txtResult.appendText("ARCHI: "+this.model.nArchi()+"\n");
        	boxPorzioni.getItems().clear();
        	boxPorzioni.getItems().addAll(this.model.getVertici(calorie));
    	} catch (NumberFormatException e) {
    		txtResult.setText("Devi inserire un numero valido");
    		return;
    	}
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        //assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}

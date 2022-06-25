/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<Track> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {
    	if(!this.model.grafoCreato()) {
    		this.doCreaGrafo(event);
    		
    		if(this.txtResult.getText().contains("Grafo creato!")) {
    			this.btnCreaLista(event);
        		return;
        	}
    		
    		return;
    	}
    	
    	Track c = cmbCanzone.getValue();
    	if(c == null) {
    		this.txtResult.setText("Seleziona una canzone!");
    		return;
    	}
    	
    	int m;
    	try {
			m = Integer.parseInt(txtMemoria.getText());
		} catch (NumberFormatException e) {
			txtResult.setText("Inserisci un numero intero per la memoria!");
			return;
		}
    	
    	String s = this.model.creaLista(c, m);
    	this.txtResult.setText(s);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Genre g = cmbGenere.getValue();
    	if(g == null) {
    		this.txtResult.setText("Seleziona un genere!");
    		return;
    	}
    	
    	String s = this.model.creaGrafo(g);
    	this.txtResult.setText(s);
    	
    	this.cmbCanzone.getItems().clear();
    	this.cmbCanzone.getItems().addAll(this.model.getVertices());
    }

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	if(!this.model.grafoCreato()) 
    		this.doCreaGrafo(event);
    	
    	
    	if(this.txtResult.getText().contains("Grafo creato!")) {
    		String s = this.model.deltaMassimo();
    		this.txtResult.setText(s);
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Genre> genres = this.model.getAllGenres();
    	
    	this.cmbGenere.getItems().clear();
    	this.cmbGenere.getItems().addAll(genres);
    }

}

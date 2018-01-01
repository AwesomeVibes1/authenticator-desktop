package awesomevibes.authenticator.desktop;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.aerogear.security.otp.Totp;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class MainController implements Initializable {
    @FXML
    private PasswordField secret;

    @FXML
    private TextField oneTimeCode;

    @FXML
    private Button generate;
    
    private final Pattern BASE32PATTERN = Pattern.compile("[2-7A-Z]+");
    
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		generate.setOnAction(this::generateOneTimePassword);
	}
	
	private void generateOneTimePassword(ActionEvent event) {
		String secretKey = secret.getText();
		
		if (secretKey.trim().isEmpty()) {
			showAlert("Please provide a secret.");
			return;
		}
		
		Matcher matcher = BASE32PATTERN.matcher(secretKey);
		if (!matcher.matches()) {
			showAlert("The provided secret is not Base32 compliant.\nIt should ony contain the characters A-Z and 2-7.");
			return;
		}
		
		try {
			Totp totp = new Totp(secretKey);
			oneTimeCode.setText(totp.now());
		} catch (Exception ex) {
			ex.printStackTrace();
			new ExceptionDialog().showException(ex);
		}
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("AV Authenticator");
		alert.setHeaderText("Warning");
		alert.setContentText(message);
		alert.showAndWait();
	}
}

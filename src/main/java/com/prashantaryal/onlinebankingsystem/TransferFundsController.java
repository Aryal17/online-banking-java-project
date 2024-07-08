package com.prashantaryal.onlinebankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONObject;

import java.io.IOException;

public class TransferFundsController {

    @FXML
    private TextField senderAccount;

    @FXML
    private TextField receiverAccount;

    @FXML
    private TextField amount;

    @FXML
    private Label responseLabel;

    @FXML
    private void transferFunds() throws IOException {
        String senderAccountText = senderAccount.getText();
        String receiverAccountText = receiverAccount.getText();
        String amountText = amount.getText();

        if (senderAccountText.isEmpty() || receiverAccountText.isEmpty() || amountText.isEmpty()) {
            responseLabel.setText("All fields are required.");
            return;
        }

        JSONObject transferData = new JSONObject();
        transferData.put("senderAccount", senderAccountText);
        transferData.put("receiverAccount", receiverAccountText);
        transferData.put("amount", amountText);

        String response = APIController.postData("http://localhost:8080/onlinebankingsystem/api/transfer", transferData.toString());

        if (response.equals("transfer_successful")) {
            responseLabel.setText("Transfer successful.");
        } else {
            responseLabel.setText("Transfer failed.");
        }
    }
}

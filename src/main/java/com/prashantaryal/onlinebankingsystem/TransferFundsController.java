package com.prashantaryal.onlinebankingsystem;

import com.prashantaryal.onlinebankingsystem.classes.Account;
import com.prashantaryal.onlinebankingsystem.classes.Transactions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TransferFundsController {

    @FXML
    private ComboBox<Account> accountFrom;

    @FXML
    private ComboBox<Account> accountTo;

    @FXML
    private TextField amount;

    @FXML
    private Label responseLabel;

    @FXML
    private TableView<Transactions> transactionsTable;

    @FXML
    private TableColumn<Transactions, Integer> transactionIdColumn;

    @FXML
    private TableColumn<Transactions, String> fromAccountColumn;

    @FXML
    private TableColumn<Transactions, String> toAccountColumn;

    @FXML
    private TableColumn<Transactions, Double> amountColumn;

    @FXML
    public void initialize() {
        loadAccounts();
        setupTransactionsTable();
        loadTransactions();
    }

    private void loadAccounts() {
        ObservableList<Account> accountsList = FXCollections.observableArrayList();
        try {
            String json = APIController.getData("http://localhost:8080/onlinebankingsystem/api/accounts", "GET");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Account account = new Account(jsonObject.getInt("id"), jsonObject.getString("accountNumber"), jsonObject.getString("owner"), jsonObject.getDouble("balance"));
                accountsList.add(account);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        accountFrom.setItems(accountsList);
        accountTo.setItems(accountsList);
    }

    private void setupTransactionsTable() {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromAccountColumn.setCellValueFactory(new PropertyValueFactory<>("accountFrom"));
        toAccountColumn.setCellValueFactory(new PropertyValueFactory<>("accountTo"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void loadTransactions() {
        ObservableList<Transactions> transactionsList = FXCollections.observableArrayList();
        try {
            String json = APIController.getData("http://localhost:8080/onlinebankingsystem/api/transfer", "GET");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int transactionId = jsonObject.getInt("transactionId");
                String fromAccount = jsonObject.getJSONObject("fromAccount").getString("accountNumber");
                String toAccount = jsonObject.getJSONObject("toAccount").getString("accountNumber");
                double amount = jsonObject.getDouble("amount");

                Transactions transaction = new Transactions(transactionId,toAccount , fromAccount, amount);
                transactionsList.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        transactionsTable.setItems(transactionsList);
    }

    @FXML
    private void transferFunds() throws IOException {
        Account senderAccount = accountFrom.getValue();
        Account receiverAccount = accountTo.getValue();
        String amount = this.amount.getText();

        if (senderAccount == null || receiverAccount == null || amount.isEmpty()) {
            responseLabel.setText("All fields are required.");
            return;
        }

        try {
            String postData = "account_id_from=" + senderAccount.getId() + "&account_id_to=" + receiverAccount.getId() + "&amount=" + amount;

            String response = APIController.sendPost("http://localhost:8080/onlinebankingsystem/api/transfer", postData);
            if (response.equals("transaction_successful")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transfer Fund");
                alert.setHeaderText(null);
                alert.setContentText("Amount Transferred Successfully \n From:" + senderAccount.getAccountNumber() + "\nTo:" + receiverAccount.getAccountNumber() + "\nAmount:" + amount);
                alert.showAndWait();
                accountFrom.setValue(null);
                accountTo.setValue(null);
                this.amount.clear();
                loadTransactions(); // Refresh the transactions table
            } else {
                responseLabel.setText(response);
                responseLabel.setStyle("-fx-text-fill: red");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseLabel.setText(" error: " + e.getMessage());
        }
    }

    @FXML
    private void goToHome() throws IOException {
        App.setRoot("home");
    }
}


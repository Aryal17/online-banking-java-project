package com.prashantaryal.onlinebankingsystem;
import com.prashantaryal.onlinebankingsystem.classes.Account;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML
    private TableView<Account> accountTable;

    @FXML
    private TableColumn<Account, Integer> idColumn;

    @FXML
    private TableColumn<Account, String> accountNumberColumn;

    @FXML
    private TableColumn<Account, String> ownerColumn;

    @FXML
    private TableColumn<Account, Double> balanceColumn;

    public void initialize() {
        // Configure the cell value factories for each column
        idColumn.setCellValueFactory(cellData -> null); // Clear any previous cell value factory
        accountNumberColumn.setCellValueFactory(cellData -> null);
        ownerColumn.setCellValueFactory(cellData -> null);
        balanceColumn.setCellValueFactory(cellData -> null);

        // Populate the TableView directly with the data
        List<Account> accounts = fetchAccountsFromDatabase();
        accountTable.getItems().addAll(accounts);

        // Set cell value factories using PropertyValueFactory (not recommended for production use)
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
    }

    private List<Account> fetchAccountsFromDatabase() {
        // Replace this with your actual database retrieval logic
        List<Account> accounts = new ArrayList<>();
        try{
            String response = APIController.getData("http://localhost:8080/onlinebankingsystem/api/accounts","GET");
            JSONArray jsonArray = new JSONArray(response);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Account account = new Account(jsonObject.getInt("id"), jsonObject.getString("accountNumber"), jsonObject.getString("owner"), jsonObject.getDouble("balance"));
                accounts.add(account);
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return accounts;
    }
}

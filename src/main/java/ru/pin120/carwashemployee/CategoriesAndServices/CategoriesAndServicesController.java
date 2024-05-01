package ru.pin120.carwashemployee.CategoriesAndServices;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CategoriesAndServicesController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private CheckBox searchCriteriaCheckbox;
    @FXML
    private Button searchButton;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button bindButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<CategoryOfServicesFX> categoriesTable;
    @FXML
    private TableColumn<CategoryOfServicesFX, String> categoryNameColumn;
    @FXML
    private TableView<ServiceFX> servicesTable;
    @FXML
    private TableColumn<ServiceFX, String> serviceNameColumn;

    private ObservableList<CategoryOfServicesFX> categoriesOfServices = FXCollections.observableArrayList();
    private ObservableList<ServiceFX> services = FXCollections.observableArrayList();
    private CategoriesOfServicesRepository categoriesOfServicesRepository = new CategoriesOfServicesRepository();
    private ServiceRepository serviceRepository = new ServiceRepository();

    Node lastSelectedTable = null;
    ResourceBundle rb;
    List<CategoryOfServices> categoriesWithServices;

    List<String> categoriesNames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        //привязываем ширину столбцов к ширине таблицы
        categoryNameColumn.prefWidthProperty().bind(categoriesTable.widthProperty());
        serviceNameColumn.prefWidthProperty().bind(servicesTable.widthProperty());

        // инициализация таблицы
        categoryNameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        serviceNameColumn.setCellValueFactory(s->s.getValue().nameProperty());
        initFillingObservableLists();
        categoriesTable.getSelectionModel().selectFirst();

        // Устанавливаем заголовки для таблиц
        //categoriesTable.setPlaceholder(new Label(resourceBundle.getString("CATEGORY_TABLE_TITLE")));
        //servicesTable.setPlaceholder(new Label(resourceBundle.getString("SERVICE_TABLE_TITLE")));

        Platform.runLater(() -> categoriesTable.requestFocus());
        lastSelectedTable = categoriesTable;
        trackingFocusOnTables();
        settingTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::bindWithCategory, this::doRefresh));
        categoriesSelectedModelListener();

        searchCriteriaCheckbox.setSelected(true);
    }

    private void initFillingObservableLists(){
        try {
            categoriesWithServices = categoriesOfServicesRepository.getCategoriesWithServices();
            fillingCategoriesAndServicesTables();
//            categoriesNames = categoriesOfServicesRepository.getCategoriesName();
//            for(int i = 0; i < categoriesNames.size(); i++){
//                categoriesOfServices.add(new CategoryOfServicesFX(categoriesNames.get(i)));
//                if(i == 0){
//                    fillingServicesObservableList(categoriesNames.get(i));
//                }
//            }

        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void fillingCategoriesAndServicesTables(){
        for(int i = 0; i < categoriesWithServices.size(); i++){
            categoriesOfServices.add(new CategoryOfServicesFX(categoriesWithServices.get(i).getCatName()));
            if(i == 0){
                fillingServicesObservableList(categoriesWithServices.get(i).getServices());
            }
        }
        categoriesTable.setItems(categoriesOfServices);
        servicesTable.setItems(services);
    }

    private Scene getActualScene(){ return categoriesTable.getScene();}
    private void fillingServicesObservableList(List<Service> servicesOfCertainCategory){
        for(Service service: servicesOfCertainCategory){
            services.add(new ServiceFX(service.getServName()));
        }
    }

    private void fillingServicesObservableList(String categoryName) throws Exception {
        List<Service> servicesOfCategory = serviceRepository.getServicesByCatName(categoryName);
        for(Service service: servicesOfCategory){
            services.add(new ServiceFX(service.getServName()));
        }
    }

    private void categoriesSelectedModelListener(){
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                services.clear();
                Optional<CategoryOfServices> categoryWithServices = categoriesWithServices.stream()
                        .filter(cs-> cs.getCatName().equals(newSelection.getName())).findFirst();
                if(categoryWithServices.isPresent()){
                        fillingServicesObservableList(categoryWithServices.get().getServices());
                        servicesTable.setItems(services);
                }
//                try {
//                    fillingServicesObservableList(newSelection.getName());
//                    servicesTable.setItems(services);
//                } catch (Exception e) {
//                    FXHelper.showErrorAlert(e.getMessage());
//                }
            }
        });
    }

    private void trackingFocusOnTables(){

        categoriesTable.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lastSelectedTable = categoriesTable;
            }
        });

        servicesTable.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lastSelectedTable = servicesTable;
            }
        });
    }

    private void settingTooltipForButtons(){
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(lastSelectedTable == categoriesTable ? new Tooltip(rb.getString("CREATE_CATEGORY")) : new Tooltip(rb.getString("CREATE_SERVICE")));
        });
        editButton.setOnMouseEntered(event -> {
            editButton.setTooltip(lastSelectedTable == categoriesTable ? new Tooltip(rb.getString("EDIT_CATEGORY")) : new Tooltip(rb.getString("EDIT_SERVICE")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(lastSelectedTable == categoriesTable ? new Tooltip(rb.getString("DELETE_CATEGORY")) : new Tooltip(rb.getString("DELETE_SERVICE")));
        });
        bindButton.setOnMouseEntered(event -> {
            bindButton.setTooltip(lastSelectedTable == categoriesTable ? new Tooltip(rb.getString("BIND_CATEGORY")) : new Tooltip(rb.getString("BIND_SERVICE")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(searchCriteriaCheckbox.isSelected() ? new Tooltip(rb.getString("SEARCH_CATEGORY")) : new Tooltip(rb.getString("SEARCH_SERVICE")));
        });


    }

    @FXML
    private void createButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.CREATE);
    }

    @FXML
    private void editButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.EDIT);
    }

    @FXML
    private void deleteButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.DELETE);
    }

    private void doOperation(FXOperationMode mode){
      if(lastSelectedTable == categoriesTable){
          CategoryOfServices categoryOfServices = switch (mode) {
              case CREATE -> new CategoryOfServices();
              case EDIT, DELETE ->
                  categoriesWithServices.stream().filter(c-> c.getCatName().equals(categoriesTable.getSelectionModel().getSelectedItem().getName())).findFirst().get();
                      //new CategoryOfServices(categoriesTable.getSelectionModel().getSelectedItem().getName(), null);
              default -> null;
          };

          if(categoryOfServices == null){
              FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
          }else{
              try {
                  Locale locale = Locale.getDefault();
                  ResourceBundle bundle = ResourceBundle.getBundle("ru.pin120.carwashemployee.CategoriesAndServices.resources.EditCategoryOfServices", locale);
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/EditCategoryOfServices.fxml"), bundle);
                  Parent root = loader.load();

                  Scene modalScene = new Scene(root);
                  Stage modalStage = new Stage();
                  modalStage.setScene(modalScene);
                  modalStage.initModality(Modality.WINDOW_MODAL);
                  modalStage.initOwner(getActualScene().getWindow());
                  modalStage.getIcons().add(AppHelper.getMainIcon());

                  EditCategoryOfServicesController categoryOfServicesController = loader.getController();
                  categoryOfServicesController.setParameters(categoryOfServices, mode, modalStage);

                  modalStage.showAndWait();
                  doResultCategoryOfServices(mode, categoryOfServicesController.getExitMode(), categoryOfServices);

              }catch (Exception e){
                  FXHelper.showErrorAlert(e.getMessage());
                  e.printStackTrace();
              }
          }
      }
    }

    private void doResultCategoryOfServices(FXOperationMode operationMode, FXFormExitMode exitMode, CategoryOfServices categoryOfServices){
        if(exitMode == FXFormExitMode.OK) {
            switch (operationMode) {
                case CREATE:
                    categoriesOfServices.add(new CategoryOfServicesFX(categoryOfServices.getCatName()));
                    // сортировка элементов в таблице
                    ObservableList<CategoryOfServicesFX> sortedCategories = FXCollections.observableArrayList(categoriesOfServices);
                    sortedCategories.sort(Comparator.comparing(CategoryOfServicesFX::getName, String::compareToIgnoreCase));
                    categoriesOfServices.setAll(sortedCategories);
                    categoriesTable.setItems(categoriesOfServices);

                    categoryOfServices.setServices(new ArrayList<>()); // чтобы не ругался на пустой список
                    categoriesWithServices.add(categoryOfServices);
                    break;
                case EDIT:
                    break;
            }
        }

        categoriesTable.requestFocus();
    }

    private void bindWithCategory(){
        if(!categoriesTable.getItems().isEmpty()) {
            if(!servicesTable.getItems().isEmpty()) {
                if(categoriesTable.getSelectionModel().getSelectedItem() != null) {
                    List<String> categoriesForBind = new ArrayList<>();
                    String selectedCatName = categoriesTable.getSelectionModel().getSelectedItem().getName();
                    int indexSelectedCategory = categoriesTable.getSelectionModel().getSelectedIndex();
                    for (CategoryOfServices categoryOfServices : categoriesWithServices) {
                        if (!selectedCatName.equals(categoryOfServices.getCatName())) {
                            categoriesForBind.add(categoryOfServices.getCatName());
                        }
                    }
//                for(String categoryOfServices: categoriesNames){
//                    if(!selectedCatName.equals(categoryOfServices)){
//                        categoriesForBind.add(categoryOfServices);
//                    }
//                }

                    try {
                        Locale locale = Locale.getDefault();
                        ResourceBundle bundle = ResourceBundle.getBundle("ru.pin120.carwashemployee.CategoriesAndServices.resources.BindWithCategory", locale);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BindWithCategory.fxml"), bundle);
                        Parent root = loader.load();

                        Scene modalScene = new Scene(root);
                        Stage modalStage = new Stage();
                        modalStage.setScene(modalScene);
                        modalStage.initModality(Modality.WINDOW_MODAL);
                        modalStage.initOwner(getActualScene().getWindow());
                        modalStage.getIcons().add(AppHelper.getMainIcon());

                        BindWithCategoryController controller = loader.getController();
                        if (lastSelectedTable == categoriesTable) {
                            controller.setParameters(modalStage, categoriesForBind, BindWithCategoryMode.CATEGORY, selectedCatName);
                            modalStage.showAndWait();
                        } else {
                            if(servicesTable.getSelectionModel().getSelectedItem() != null) {
                                controller.setParameters(modalStage, categoriesForBind, BindWithCategoryMode.SERVICE, servicesTable.getSelectionModel().getSelectedItem().getName());
                                modalStage.showAndWait();
                            }else{
                                FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_SERVICE"));
                            }
                        }

                        if (controller.getExitMode() == FXFormExitMode.OK) {
                            doRefresh();
                            categoriesTable.getSelectionModel().select(indexSelectedCategory);
                        }

                    } catch (Exception e) {
                        FXHelper.showErrorAlert(e.getMessage());
                    }
                }else{
                    FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
                }
            }else{
                FXHelper.showErrorAlert(rb.getString("NOT_EXISTS_SERVICES_FOR_BIND"));
            }
        }else{
            FXHelper.showErrorAlert(rb.getString("NOT_EXISTS_CATEGORY_FOR_BIND"));
        }
    }

    public void bindButtonAction(ActionEvent actionEvent) {
        bindWithCategory();
    }

    private void doClearData(){
        categoriesTable.setItems(null);
        servicesTable.setItems(null);
        categoriesOfServices.clear();
        services.clear();
    }
    private void doRefresh(){
        doClearData();

        initFillingObservableLists();

        categoriesTable.requestFocus();
        if(!categoriesTable.getItems().isEmpty()){
            categoriesTable.getSelectionModel().selectFirst();
        }

        searchField.clear();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }


    public void searchButtonAction(ActionEvent actionEvent) {
        doSearch();
    }

    private void doSearch(){
        if(searchField.getText() == null || searchField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("SEARCH_FIELD_IS_EMPTY"));
            searchField.requestFocus();
        }else{
            String searchParameter = searchField.getText().trim().toLowerCase();
            doClearData();
            if(searchCriteriaCheckbox.isSelected()){

                categoriesWithServices = categoriesWithServices.stream()
                        .filter(c-> c.getCatName().toLowerCase().contains(searchParameter))
                        .toList();
                fillingCategoriesAndServicesTables();

                categoriesTable.requestFocus();
                categoriesTable.getSelectionModel().selectFirst();

            }else{

            }
        }
    }
}

package ru.pin120.carwashemployee.CategoriesAndServices;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.PriceListPosition.PriceListPositionController;

import java.net.URL;
import java.util.*;

public class CategoriesAndServicesController implements Initializable {

    @FXML
    private CheckBox searchCategoryCheckBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button setServicePriceAndTimeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button createButton;
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

    String lastSearchedCategory = "";
    String lastSearchedService = "";

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

        searchCategoryCheckBox.setSelected(true);
    }

    private void initFillingObservableLists(){
        try {
//            categoriesWithServices = categoriesOfServicesRepository.getCategoriesWithServices();
//            fillingCategoriesAndServicesTables();
            categoriesNames = categoriesOfServicesRepository.getCategoriesName();
            for(int i = 0; i < categoriesNames.size(); i++){
                categoriesOfServices.add(new CategoryOfServicesFX(categoriesNames.get(i)));
                if(i == 0){
                    fillingServicesObservableList(categoriesNames.get(i));
                }
            }

        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }

        categoriesTable.setItems(categoriesOfServices);
        servicesTable.setItems(services);
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
//                Optional<CategoryOfServices> categoryWithServices = categoriesWithServices.stream()
//                        .filter(cs-> cs.getCatName().equals(newSelection.getName())).findFirst();
//                if(categoryWithServices.isPresent()){
//                        fillingServicesObservableList(categoryWithServices.get().getServices());
//                        servicesTable.setItems(services);
//                }
                try {
                    fillingServicesObservableList(newSelection.getName());
                    servicesTable.setItems(services);

//                    //проверка
//                    for(ServiceFX serviceFX: services){
//                        if(serviceFX.getName().equals(lastSearchedService)){
//                            servicesTable.getSelectionModel().select(serviceFX);
//                            break;
//                        }
//                    }
                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                }
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
//        editButton.setOnMouseEntered(event -> {
//            editButton.setTooltip(lastSelectedTable == categoriesTable ? new Tooltip(rb.getString("EDIT_CATEGORY")) : new Tooltip(rb.getString("EDIT_SERVICE")));
//        });
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
            searchButton.setTooltip(searchCategoryCheckBox.isSelected() ? new Tooltip(rb.getString("SEARCH_CATEGORY")) : new Tooltip(rb.getString("SEARCH_SERVICE")));
        });
        setServicePriceAndTimeButton.setOnMouseEntered(event->{
            setServicePriceAndTimeButton.setTooltip(new Tooltip(rb.getString("SET_PRICE_AND_TIME_SERVICE")));
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
        if(mode != FXOperationMode.EDIT) {
            if (lastSelectedTable == categoriesTable) {
                CategoryOfServicesFX selectedCategoryOfServicesFX = null;
                //int selectedItemIndex = -1;
                CategoryOfServices categoryOfServices = null;
                switch (mode) {
                    case CREATE:
                        categoryOfServices = new CategoryOfServices();
                        break;
                    case EDIT:
                    case DELETE:
                        if (categoriesTable.getSelectionModel().getSelectedItem() != null) {
                            categoryOfServices = new CategoryOfServices();
                            categoryOfServices.setCatName(categoriesTable.getSelectionModel().getSelectedItem().getName());
                            selectedCategoryOfServicesFX = categoriesTable.getSelectionModel().getSelectedItem();
                            //selectedItemIndex = categoriesTable.getSelectionModel().getSelectedIndex();
                        }
                        break;
                }

                if (categoryOfServices == null) {
                    FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
                    categoriesTable.requestFocus();
                } else {
                    try {

                        FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.CategoriesAndServices.resources.EditCategoryOfServices", "CategoriesAndServices/fxml/EditCategoryOfServices.fxml", getActualScene());

                        EditCategoryOfServicesController categoryOfServicesController = fxWindowData.getLoader().getController();
                        categoryOfServicesController.setParameters(categoryOfServices, mode, fxWindowData.getModalStage());

                        fxWindowData.getModalStage().showAndWait();
                        doResultCategoryOfServices(mode, categoryOfServicesController.getExitMode(), categoryOfServices, selectedCategoryOfServicesFX);

                    } catch (Exception e) {
                        FXHelper.showErrorAlert(e.getMessage());
                        categoriesTable.requestFocus();
                    }
                }
            } else {
                ServiceDTO serviceDTO = null;
                if (categoriesTable.getSelectionModel().getSelectedItem() == null) {
                    FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
                    servicesTable.requestFocus();
                } else {
                    String catName = categoriesTable.getSelectionModel().getSelectedItem().getName();
                    ServiceFX selectedServiceFX = null;
                    switch (mode) {
                        case CREATE:
                            serviceDTO = new ServiceDTO();
                            serviceDTO.setCatName(catName);
                            break;
                        case DELETE:
                            if (servicesTable.getSelectionModel().getSelectedItem() != null) {
                                serviceDTO = new ServiceDTO();
                                serviceDTO.setCatName(catName);
                                serviceDTO.setServName(servicesTable.getSelectionModel().getSelectedItem().getName());
                                selectedServiceFX = servicesTable.getSelectionModel().getSelectedItem();
                            }
                            break;
                    }
                    if (serviceDTO == null) {
                        FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_SERVICE"));
                        servicesTable.requestFocus();
                    } else {
                        try {
                            FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.CategoriesAndServices.resources.EditService", "CategoriesAndServices/fxml/EditService.fxml", getActualScene());

                            EditServiceController serviceController = fxWindowData.getLoader().getController();
                            serviceController.setParameters(serviceDTO, mode, fxWindowData.getModalStage());

                            fxWindowData.getModalStage().showAndWait();
                            doResultService(mode, serviceController.getExitMode(), serviceDTO, selectedServiceFX);
                        } catch (Exception e) {
                            FXHelper.showErrorAlert(e.getMessage());
                            servicesTable.requestFocus();
                        }
                    }
                }
            }
        }
    }

    private void doResultCategoryOfServices(FXOperationMode operationMode, FXFormExitMode exitMode, CategoryOfServices categoryOfServices, CategoryOfServicesFX selectedCategoryOfServicesFX){
        if(exitMode == FXFormExitMode.OK) {
            switch (operationMode) {
                case CREATE:
                    //if(lastSearchedCategory.isEmpty() || categoryOfServices.getCatName().toLowerCase().contains(lastSearchedCategory.toLowerCase())) {
                        categoryOfServices.setServices(new ArrayList<>()); // чтобы не ругался на пустой список
                        CategoryOfServicesFX categoryOfServicesFX = new CategoryOfServicesFX(categoryOfServices.getCatName());
                        categoriesOfServices.add(categoryOfServicesFX);
                        // сортировка элементов в таблице
                        ObservableList<CategoryOfServicesFX> sortedCategories = FXCollections.observableArrayList(categoriesOfServices);
                        sortedCategories.sort(Comparator.comparing(CategoryOfServicesFX::getName, String::compareToIgnoreCase));
                        categoriesOfServices.setAll(sortedCategories);
                        categoriesTable.setItems(categoriesOfServices);
                    //}
                        categoriesTable.getSelectionModel().select(categoryOfServicesFX);
                        categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
//                case EDIT:
//                        categoriesOfServices.remove(selectedCategoryOfServicesFX);
//                        categoriesOfServices.add(new CategoryOfServicesFX(categoryOfServices.getCatName()));
//                        ObservableList<CategoryOfServicesFX> sortedCategoriesEd = FXCollections.observableArrayList(categoriesOfServices);
//                        sortedCategoriesEd.sort(Comparator.comparing(CategoryOfServicesFX::getName, String::compareToIgnoreCase));
//                        categoriesOfServices.setAll(sortedCategoriesEd);
//                        categoriesTable.setItems(categoriesOfServices);
//                    break;
                case DELETE:
                    categoriesOfServices.remove(selectedCategoryOfServicesFX);
            }
        }

        categoriesTable.requestFocus();
    }

    private void doResultService(FXOperationMode operationMode, FXFormExitMode exitMode, ServiceDTO serviceDTO, ServiceFX selectedServiceFX){
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    //if(lastSearchedService.isEmpty()) {
                        ServiceFX serviceFX = new ServiceFX(serviceDTO.getServName());
                        services.add(serviceFX);
                        ObservableList<ServiceFX> sortedServices = FXCollections.observableArrayList(services);
                        sortedServices.sort(Comparator.comparing(ServiceFX::getName, String::compareToIgnoreCase));
                        services.setAll(sortedServices);
                        servicesTable.setItems(services);

                        //servicesTable.getSelectionModel().selectFirst();
                        serviceNameColumn.setSortType(TableColumn.SortType.ASCENDING);
                        servicesTable.getSelectionModel().select(serviceFX);
                    //}
                    break;
                case EDIT:
                    break;
                case DELETE:
                    services.remove(selectedServiceFX);
            }
        }

        servicesTable.requestFocus();
    }



    private void bindWithCategory(){
        if(!categoriesTable.getItems().isEmpty()) {
            if(!servicesTable.getItems().isEmpty()) {
                if(categoriesTable.getSelectionModel().getSelectedItem() != null) {
                    //List<String> categoriesForBind = new ArrayList<>();

                    ServiceFX selectedServiceFX = null;
                    String selectedCatName = categoriesTable.getSelectionModel().getSelectedItem().getName();
                    int indexSelectedCategory = categoriesTable.getSelectionModel().getSelectedIndex();
//                    for (CategoryOfServices categoryOfServices : categoriesWithServices) {
//                        if (!selectedCatName.equals(categoryOfServices.getCatName())) {
//                            categoriesForBind.add(categoryOfServices.getCatName());
//                        }
//                    }
//                for(String categoryOfServices: categoriesNames){
//                    if(!selectedCatName.equals(categoryOfServices)){
//                        categoriesForBind.add(categoryOfServices);
//                    }
//                }

                    try {
                        FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.CategoriesAndServices.resources.BindWithCategory", "CategoriesAndServices/fxml/BindWithCategory.fxml", getActualScene());


                        BindWithCategoryController controller = fxWindowData.getLoader().getController();
                        if (lastSelectedTable == categoriesTable) {
                            controller.setParameters(fxWindowData.getModalStage(), BindWithCategoryMode.CATEGORY, selectedCatName,selectedCatName);
                            fxWindowData.getModalStage().showAndWait();
                        } else {
                            if(servicesTable.getSelectionModel().getSelectedItem() != null) {
                                selectedServiceFX = servicesTable.getSelectionModel().getSelectedItem();
                                controller.setParameters(fxWindowData.getModalStage(),BindWithCategoryMode.SERVICE, selectedCatName, servicesTable.getSelectionModel().getSelectedItem().getName());
                                fxWindowData.getModalStage().showAndWait();
                            }else{
                                FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_SERVICE"));
                            }
                        }

//                        if (controller.getExitMode() == FXFormExitMode.OK) {
//                            doRefresh();
//                            categoriesTable.getSelectionModel().select(indexSelectedCategory);
//                        }
                        if (controller.getExitMode() == FXFormExitMode.OK) {
                            if(lastSelectedTable == categoriesTable){
                                services.clear();
                            }else{
                                if(selectedServiceFX != null){
                                    services.remove(selectedServiceFX);
                                }
                            }
                            categoriesTable.getSelectionModel().select(indexSelectedCategory);
                        }

                    } catch (Exception e) {
                        FXHelper.showErrorAlert(e.getMessage());
                        categoriesTable.requestFocus();
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

        categoriesTable.requestFocus();
    }

    public void bindButtonAction(ActionEvent actionEvent) {
        bindWithCategory();
    }

    private void doClearData(){
        //categoriesTable.setItems(null);
        //servicesTable.setItems(null);
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
        lastSearchedCategory = "";
        lastSearchedService = "";

        categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        serviceNameColumn.setSortType(TableColumn.SortType.ASCENDING);

    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }


    public void searchButtonAction(ActionEvent actionEvent) {
        doSearch();
    }

    private void doSearch(){
        lastSearchedService = "";
        lastSearchedCategory = "";

        if(searchField.getText() == null || searchField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("SEARCH_FIELD_IS_EMPTY"));
            searchField.requestFocus();
        }else{
            String searchParameter = searchField.getText().trim();
            categoriesOfServices.clear();
            services.clear();
            if(searchCategoryCheckBox.isSelected()){
                try {
                    List<String> sortedCategories = categoriesOfServicesRepository.getCategoriesNameByParameter(searchParameter);
                    for(int i = 0; i < sortedCategories.size(); i++){
                        categoriesOfServices.add(new CategoryOfServicesFX(sortedCategories.get(i)));
                        if(i == 0){
                            fillingServicesObservableList(sortedCategories.get(i));
                        }
                    }
                    if(!categoriesOfServices.isEmpty()){
                        categoriesTable.requestFocus();
                        categoriesTable.getSelectionModel().selectFirst();
                        //lastSearchedCategory = searchParameter;
                    }else{
                        searchField.requestFocus();
                    }

                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                    categoriesTable.requestFocus();
                }
            }else{
                try{
                    ServiceDTO findServiceWithCategory = serviceRepository.getServiceDTOByServName(searchParameter);
                    if(findServiceWithCategory.getServName() != null){
                        categoriesOfServices.add(new CategoryOfServicesFX(findServiceWithCategory.getCatName()));
                        services.add(new ServiceFX(findServiceWithCategory.getServName()));
                        categoriesTable.getSelectionModel().selectFirst();

                        servicesTable.requestFocus();

                        //устанавливаем фокус на найденную услугу
                        for(ServiceFX serviceFX: services){
                            if(serviceFX.getName().equals(searchParameter)){
                                servicesTable.getSelectionModel().select(serviceFX);
                                //servicesTable.getFocusModel().focus(servicesTable.getSelectionModel().getSelectedIndex());
                                break;
                            }
                        }

                        //lastSearchedService = searchParameter;
                    }else{
                        refreshButton.requestFocus();
                    }
                }catch (Exception e){
                    FXHelper.showErrorAlert(e.getMessage());
                    servicesTable.requestFocus();
                }
            }
//            String searchParameter = searchField.getText().trim().toLowerCase();
//            doClearData();
//            if(searchCategoryCheckBox.isSelected()){
//
//                categoriesWithServices = categoriesWithServices.stream()
//                        .filter(c-> c.getCatName().toLowerCase().contains(searchParameter))
//                        .toList();
//                fillingCategoriesAndServicesTables();
//
//                categoriesTable.requestFocus();
//                categoriesTable.getSelectionModel().selectFirst();
//
//            }else{
//
//            }

        }
    }

    public void setServicePriceAndTimeButtonAction(ActionEvent actionEvent) {
        try {
            ServiceFX selectedServiceFX = servicesTable.getSelectionModel().getSelectedItem();
            if (selectedServiceFX == null) {
                FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_SERVICE"));
                servicesTable.requestFocus();
            } else {
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.PriceListPosition.resources.PriceListPosition", "PriceListPosition/fxml/PriceListPosition.fxml", getActualScene());
                PriceListPositionController priceListPositionController = fxWindowData.getLoader().getController();
                priceListPositionController.setParameters(selectedServiceFX.getName(), fxWindowData.getModalStage());

                fxWindowData.getModalStage().showAndWait();
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            servicesTable.requestFocus();
        }
    }
}
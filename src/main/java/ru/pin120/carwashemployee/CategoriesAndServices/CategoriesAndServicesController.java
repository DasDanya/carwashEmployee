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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.*;

public class CategoriesAndServicesController implements Initializable {

    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
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

    Node lastSelectedTable = null;
    ResourceBundle rb;
    List<CategoriesWithServicesDTO> categoriesWithServices;



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
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation));
        categoriesSelectedModelListener();
    }

    private void initFillingObservableLists(){
        try {
            categoriesWithServices = categoriesOfServicesRepository.getCategoriesWithServices();
            for(int i = 0; i < categoriesWithServices.size(); i++){
                categoriesOfServices.add(new CategoryOfServicesFX(categoriesWithServices.get(i).getCategoryName()));
                if(i == 0){
                    if(!categoriesWithServices.get(i).getServicesOfCategory().isEmpty()) {
                        fillingServicesObservableList(categoriesWithServices.get(i).getServicesOfCategory());
                    }
                }
            }
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }

        categoriesTable.setItems(categoriesOfServices);
        servicesTable.setItems(services);
    }

    private Scene getActualScene(){ return categoriesTable.getScene();}
    private void fillingServicesObservableList(List<String> servicesOfCertainCategory){
        for(String service: servicesOfCertainCategory){
            services.add(new ServiceFX(service));
        }
    }

    private void categoriesSelectedModelListener(){
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                services.clear();
                Optional<CategoriesWithServicesDTO> categoryWithServices = categoriesWithServices.stream()
                        .filter(cs-> cs.getCategoryName().equals(newSelection.getName())).findFirst();
                if(categoryWithServices.isPresent()){

                        fillingServicesObservableList(categoryWithServices.get().getServicesOfCategory());
                        servicesTable.setItems(services);
                        System.out.println(categoryWithServices.get().getCategoryName() + "\n" + categoryWithServices.get().getServicesOfCategory().toString() + categoryWithServices.get().getServicesOfCategory().size());

                }
                //categoryWithServices.ifPresent(categoriesWithServicesDTO -> fillingServicesObservableList(categoriesWithServicesDTO.getServicesOfCategory()));
                //servicesTable.setItems(services);

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
                      new CategoryOfServices(categoriesTable.getSelectionModel().getSelectedItem().getName());
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

                  if(categoryOfServicesController.getExitMode() == FXFormExitMode.OK){
                      doResultCategoryOfServices(mode, categoryOfServices);
                  }
              }catch (Exception e){
                  FXHelper.showErrorAlert(e.getMessage());
                  e.printStackTrace();
              }
          }
      }
    }

    private void doResultCategoryOfServices(FXOperationMode operationMode, CategoryOfServices categoryOfServices){
        switch (operationMode){
            case CREATE:
                categoriesOfServices.add(new CategoryOfServicesFX(categoryOfServices.getCatName()));
                ObservableList<CategoryOfServicesFX> sortedCategories = FXCollections.observableArrayList(categoriesOfServices);
                sortedCategories.sort(Comparator.comparing(CategoryOfServicesFX::getName, String::compareToIgnoreCase));
                categoriesOfServices.setAll(sortedCategories);
                categoriesTable.setItems(categoriesOfServices);

                //добавим в изначальный список
                CategoriesWithServicesDTO categoriesWithServicesDTO = new CategoriesWithServicesDTO(categoryOfServices.getCatName(), new ArrayList<>(0));
                categoriesWithServices.add(categoriesWithServicesDTO);
                for(CategoriesWithServicesDTO check: categoriesWithServices){
                    //System.out.println(check.getCategoryName() + "\n" + check.getServicesOfCategory().toString());
                }

                //categoriesTable.getSelectionModel().select(categoriesTable.getSelectionModel().getFocusedIndex());
                break;
            case EDIT:
                break;
        }

        categoriesTable.requestFocus();
    }


}

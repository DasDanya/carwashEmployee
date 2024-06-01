package ru.pin120.carwashemployee.Bookings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.LocalTimeTextField;
import lombok.Getter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransportFX;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransportRepository;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.PriceListPosition.PriceListPositionRepository;
import ru.pin120.carwashemployee.PriceListPosition.ServiceInPriceListSelectable;
import ru.pin120.carwashemployee.PriceListPosition.ServiceWithPriceList;
import ru.pin120.carwashemployee.PriceListPosition.ServiceWithPriceListFX;



import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class EditBookingController implements Initializable, ServiceInPriceListSelectable {

    @FXML
    private LocalTimeTextField bookingStartTimeField;
    @FXML
    private ComboBox<BookingStatus> statusComboBox;
    @FXML
    private Label infoPriceLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label infoTimeExecuteLabel;
    @FXML
    private CheckBox searchCategoryCheckBox;
    @FXML
    private TextField searchClTransportField;
    @FXML
    private TextField searchServiceField;
    @FXML
    private Button searchClrTransportButton;
    @FXML
    private Button showClientButton;
    @FXML
    private Button searchServButton;
    @FXML
    private Button btOK;
    @FXML
    private TableView<ClientsTransportFX> clientTransportTable;
    @FXML
    private TableColumn<ClientsTransportFX, String> markColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> modelColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> categoryTrColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> stageNumberColumn;
    @FXML
    private TableView<ServiceWithPriceListFX> priceListTable;
    @FXML
    private TableColumn<ServiceWithPriceListFX, CheckBox> selectColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, String> categoryServColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, String> serviceColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, Integer> priceColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, Integer> timeColumn;

    private ResourceBundle rb;
    private ClientsTransportRepository clientsTransportRepository = new ClientsTransportRepository();
    private PriceListPositionRepository priceListPositionRepository = new PriceListPositionRepository();
    private BookingsRepository bookingsRepository = new BookingsRepository();
    private FXOperationMode operationMode;
    @Getter
    private FXFormExitMode exitMode;
    @FXML
    private Stage stage;
    private Box box;
    private Booking booking;
    private ClientsTransport selectedClientTransport;
    private List<ClientsTransport> clientsTransportList = new ArrayList<>();
    private List<ServiceWithPriceList> serviceWithPriceLists = new ArrayList<>();
    private ObservableList<ClientsTransportFX> clientsTransportFXES = FXCollections.observableArrayList();
    private ObservableList<ServiceWithPriceListFX> serviceWithPriceListFXES =FXCollections.observableArrayList();

    private int price;
    private int timeExecute;
    private LocalDateTime selectedDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        fillingBookingStatusComboBox();

        modelColumn.setCellValueFactory(ct->ct.getValue().clTrModelProperty());
        markColumn.setCellValueFactory(ct->ct.getValue().clTrMarkProperty());
        categoryTrColumn.setCellValueFactory(ct->ct.getValue().clTrCategoryProperty());
        stageNumberColumn.setCellValueFactory(ct->ct.getValue().clTrStateNumberProperty());

        selectColumn.setCellValueFactory(p->p.getValue().selectProperty());
        categoryServColumn.setCellValueFactory(p->p.getValue().catNameProperty());
        serviceColumn.setCellValueFactory(p->p.getValue().servNameProperty());
        timeColumn.setCellValueFactory(p->p.getValue().plTimeProperty().asObject());
        priceColumn.setCellValueFactory(p->p.getValue().plPriceProperty().asObject());

        FXHelper.setContextMenuForEditableTextField(searchServiceField);
        FXHelper.setContextMenuForEditableTextField(searchClTransportField);

        clientTransportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        priceListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        stateNumberFieldListener();
        tableSelectModelListener();
        setTooltipForButtons();
    }


    private void stateNumberFieldListener() {
        searchClTransportField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ClientsTransportFX.MAX_STATE_NUMBER_LENGTH) {
                    searchClTransportField.setText(oldValue);
                }
            }
        });
    }

    private void setTooltipForButtons() {
        searchClrTransportButton.setOnMouseEntered(event -> {
            searchClrTransportButton.setTooltip(new Tooltip(rb.getString("SEARCH_TRANSPORT")));
        });
        searchServButton.setOnMouseEntered(event -> {
            searchServButton.setTooltip(new Tooltip(rb.getString("SEARCH_SERVICES")));
        });
        showClientButton.setOnMouseEntered(event -> {
            showClientButton.setTooltip(new Tooltip(rb.getString("SHOW_CLIENT_DATA")));
        });
    }

    public void settingForm(LocalDateTime selectedDate, FXOperationMode operationMode, Stage stage, Box box, Booking booking){
        this.selectedDate = selectedDate;
        this.operationMode = operationMode;
        this.stage = stage;
        this.box = box;
        this.booking = booking;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(String.format(rb.getString("CREATE_TITLE"),this.box.getBoxId(), this.selectedDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
                searchServButton.setDisable(true);
                statusComboBox.getSelectionModel().select(BookingStatus.BOOKED);
                statusComboBox.setDisable(true);
                LocalTime now = LocalTime.now();
                if(now.isBefore(AppHelper.startWorkTime()) || !now.isBefore(AppHelper.endWorkTime())){
                    //bookingStartTimeField.setEditable(false);
                }
                bookingStartTimeField.setLocalTime(now);
                if(this.selectedDate.toLocalDate().equals(LocalDate.now())) {
                    LocalTime startTime = now.minusMinutes(5);
                    if(startTime.isBefore(AppHelper.startWorkTime())) {
                        startTime = AppHelper.startWorkTime();
                    }
                    settingTimeSpinner(LocalTime.of(startTime.getHour(), startTime.getMinute()));
                }else{
                    settingTimeSpinner(AppHelper.startWorkTime());
                }

                setPriceLabelText(price);
                setTimeExecuteLabelText();
                //Platform.runLater(()->searchClTransportField.requestFocus());
                break;
        }

        this.stage.setMaximized(true);
    }

    private void settingTimeSpinner(LocalTime startTime){
        bookingStartTimeField.setParseErrorCallback(throwable -> null);

        bookingStartTimeField.localTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && (newValue.isBefore(LocalTime.of(startTime.getHour(), startTime.getMinute())) || !newValue.isBefore(AppHelper.endWorkTime()))) {
                bookingStartTimeField.setLocalTime(oldValue);
            }else {
                setTimeExecuteLabelText();
            }
        });
    }

    private void fillingBookingStatusComboBox(){
        statusComboBox.getItems().setAll(BookingStatus.values());
        statusComboBox.setConverter(new StringConverter<BookingStatus>() {
            @Override
            public String toString(BookingStatus status) {
                return status.getDisplayValue();
            }
            @Override
            public BookingStatus fromString(String string) {
                return null;
            }
        });
    }


    public void searchClrTransportButtonAction(ActionEvent actionEvent) {
        if(searchClTransportField.getText() == null || searchClTransportField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("NECCESARY_INPUT_STATE_NUMBER"));
        }else {
            clientsTransportFXES.clear();
            clientsTransportList.clear();
            try {
                clientsTransportList = clientsTransportRepository.getByStateNumber(searchClTransportField.getText().trim());

                for (ClientsTransport ct : clientsTransportList) {
                    ClientsTransportFX clientsTransportFX = new ClientsTransportFX(ct.getClTrId(), ct.getTransport().getTrMark(), ct.getTransport().getTrModel(), ct.getTransport().getCategoryOfTransport().getCatTrName(), ct.getClTrStateNumber());
                    clientsTransportFXES.add(clientsTransportFX);
                }

                clientTransportTable.setItems(clientsTransportFXES);
                if(!clientsTransportFXES.isEmpty()) {
                    clientTransportTable.requestFocus();
                    clientTransportTable.getSelectionModel().selectFirst();
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                clientTransportTable.requestFocus();
            }

        }
    }

    public void showClientButtonAction(ActionEvent actionEvent) {
        if(selectedClientTransport == null){
            FXHelper.showErrorAlert(rb.getString("TRANSPORT_NOT_EXISTS"));
        }else{
            Client client = selectedClientTransport.getClient();
            String clientData = String.format(rb.getString("CLIENT_DATA"), client.getClSurname(), client.getClName(), client.getClPhone(), client.getClDiscount());
            FXHelper.showInfoAlert(clientData);
        }

        clientTransportTable.requestFocus();
    }

    private void tableSelectModelListener() {
        clientTransportTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->  {
            serviceWithPriceListFXES.clear();
            serviceWithPriceLists.clear();
            searchServButton.setDisable(true);
            price = 0;
            setPriceLabelText(price);
            timeExecute = 0;
            setTimeExecuteLabelText();

            if(newValue != null){
                selectedClientTransport = getSelectedClientTransport(newValue);
                if(selectedClientTransport == null){
                    FXHelper.showErrorAlert(rb.getString("TRANSPORT_NOT_EXISTS"));
                }else{
                    if(selectedClientTransport.getClient().getClDiscount() > 0){
                        infoPriceLabel.setText(String.format(rb.getString("INFO_PRICE_WITH_DISCOUNT"), selectedClientTransport.getClient().getClDiscount()));
                    }else{
                        infoPriceLabel.setText(rb.getString("INFO_PRICE"));
                    }
                    try {
                        serviceWithPriceLists = priceListPositionRepository.getCategoryOfTransportPriceList(selectedClientTransport.getTransport().getCategoryOfTransport().getCatTrId());
                        for(ServiceWithPriceList serviceWithPriceList: serviceWithPriceLists){
                            ServiceWithPriceListFX serviceWithPriceListFX = new ServiceWithPriceListFX(new CheckBox(), serviceWithPriceList.getCatName(), serviceWithPriceList.getServName(), serviceWithPriceList.getPlPrice(), serviceWithPriceList.getPlTime(),this);
                            serviceWithPriceListFXES.add(serviceWithPriceListFX);
                        }
                        priceListTable.setItems(serviceWithPriceListFXES);
                        if(!serviceWithPriceListFXES.isEmpty()) {
                            searchServButton.setDisable(false);
                            //priceListTable.requestFocus();
                            //priceListTable.getSelectionModel().selectFirst();
                        }
                    }catch (Exception e){
                        FXHelper.showErrorAlert(e.getMessage());
                    }
                }
            }
        });
    }

    private ClientsTransport getSelectedClientTransport(ClientsTransportFX transportFX){
        return clientsTransportList.stream()
                .filter(c-> c.getClTrId() == transportFX.getClTrId())
                .findFirst()
                .orElse(null);
    }

    public void searchServButtonAction(ActionEvent actionEvent) {
        if(searchServiceField.getText() == null || searchServiceField.getText().isBlank()){
            priceListTable.setItems(serviceWithPriceListFXES);
        }else {
            String parameter = searchServiceField.getText().trim().toLowerCase();
            ObservableList<ServiceWithPriceListFX> searchedServicesWithPriceListFXES = FXCollections.observableArrayList();
            if (searchCategoryCheckBox.isSelected()) {
                searchedServicesWithPriceListFXES = serviceWithPriceListFXES.filtered(s->s.getCatName().toLowerCase().contains(parameter));
            } else {
                searchedServicesWithPriceListFXES = serviceWithPriceListFXES.filtered(s->s.getServName().toLowerCase().contains(parameter));
            }

            List<ServiceWithPriceListFX> sortedList = new ArrayList<>(searchedServicesWithPriceListFXES);
            sortedList.sort(Comparator.comparing(ServiceWithPriceListFX::getCatName, String::compareToIgnoreCase)
                    .thenComparing(ServiceWithPriceListFX::getServName, String::compareToIgnoreCase));


            searchedServicesWithPriceListFXES = FXCollections.observableArrayList(sortedList);
            priceListTable.setItems(searchedServicesWithPriceListFXES);
        }

        priceListTable.getSelectionModel().selectFirst();
        priceListTable.requestFocus();
    }

    @Override
    public void onCheckBoxChanged(ServiceWithPriceListFX service, boolean isNowSelected) {
        if(isNowSelected){
            price += service.getPlPrice();
            timeExecute += service.getPlTime();
        }else{
            price -= service.getPlPrice();
            timeExecute -= service.getPlTime();
        }


        setPriceLabelText(getPriceWithDiscount());
        setTimeExecuteLabelText();
    }

    private int getPriceWithDiscount(){
        double discountedPrice = price * (1 - (selectedClientTransport.getClient().getClDiscount() / 100.0));
        return (int) Math.floor(discountedPrice);
    }


    private void setPriceLabelText(int pr){
        priceLabel.setText(String.format(rb.getString("PRICE_LABEL"), pr));
    }

    private void setTimeExecuteLabelText(){
        LocalTime totalExecuteTime = bookingStartTimeField.getLocalTime().withSecond(0).withNano(0).plusMinutes(timeExecute);
        infoTimeExecuteLabel.setText(String.format(rb.getString("INFO_TIME_EXECUTE_VALUE"), totalExecuteTime.toString()));
    }


    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        long minutesDifference = bookingStartTimeField.getLocalTime().until(AppHelper.endWorkTime(), ChronoUnit.MINUTES);
        if(timeExecute > minutesDifference){
            FXHelper.showErrorAlert(String.format(rb.getString("EXCEEDING_TIME_LIMIT"), AppHelper.endWorkTime().toString()));
            priceListTable.requestFocus();
        }else{
            switch (operationMode){
                case CREATE:
                    List<ServiceWithPriceList> services = new ArrayList<>();
                    for (ServiceWithPriceListFX serviceWithPriceListFX: serviceWithPriceListFXES){
                        if(serviceWithPriceListFX.getSelect().isSelected()){
                            ServiceWithPriceList service = new ServiceWithPriceList(serviceWithPriceListFX.getCatName(), serviceWithPriceListFX.getServName(), serviceWithPriceListFX.getPlPrice(), serviceWithPriceListFX.getPlTime());
                            services.add(service);
                        }
                    }
                    if(services.isEmpty()){
                        FXHelper.showErrorAlert(rb.getString("LIST_OF_SERVICES_NOT_EMPTY"));
                    }else {
                        LocalTime startExecuteTime = bookingStartTimeField.getLocalTime();
                        LocalTime totalExecuteTime = startExecuteTime.plusMinutes(timeExecute);
                        BookingDTO bookingDTO = new BookingDTO();
                        bookingDTO.setBkStartTime(getLocalDateTimeWithHoursAndMinutes(selectedDate.withHour(startExecuteTime.getHour()).withMinute(startExecuteTime.getMinute())));
                        bookingDTO.setBkEndTime(getLocalDateTimeWithHoursAndMinutes(selectedDate.withHour(totalExecuteTime.getHour()).withMinute(totalExecuteTime.getMinute())));
                        bookingDTO.setBox(box);
                        bookingDTO.setClientTransport(selectedClientTransport);
                        bookingDTO.setServices(services);

                        try {
                            Booking createdBooking = bookingsRepository.create(bookingDTO);
                            if(createdBooking != null){
                                canExit = true;
                            }
                        }catch (Exception e){
                            FXHelper.showErrorAlert(e.getMessage());
                        }
                    }

                    break;
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }
    }

    private LocalDateTime getLocalDateTimeWithHoursAndMinutes(LocalDateTime localDateTime){
        return localDateTime.withSecond(0).withNano(0);
    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }
}

package ru.pin120.carwashemployee.Users;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserFX {
    private LongProperty usId;
    private StringProperty usName;
    private StringProperty usRole;

    public long getUsId() {
        return usId.get();
    }

    public LongProperty usIdProperty() {
        return usId;
    }

    public String getUsName() {
        return usName.get();
    }

    public StringProperty usNameProperty() {
        return usName;
    }

    public String getUsRole() {
        return usRole.get();
    }

    public StringProperty usRoleProperty() {
        return usRole;
    }

    public UserFX(Long usId, String usName, String usRole) {
        this.usId = new SimpleLongProperty(usId);
        this.usName = new SimpleStringProperty(usName);
        this.usRole = new SimpleStringProperty(usRole);
    }
}

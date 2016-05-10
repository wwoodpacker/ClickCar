package com.taxi.clickcar.Order;

import java.util.ArrayList;

/**
 * Created by Назар on 22.04.2016.
 */
public class Cost {
    public Cost(){}
    private String user_full_name;

    public String getUserFullName() { return this.user_full_name; }

    public void setUserFullName(String user_full_name) { this.user_full_name = user_full_name; }

    private String user_phone;

    public String getUserPhone() { return this.user_phone; }

    public void setUserPhone(String user_phone) { this.user_phone = user_phone; }

    private Object client_sub_card;

    public Object getClientSubCard() { return this.client_sub_card; }

    public void setClientSubCard(Object client_sub_card) { this.client_sub_card = client_sub_card; }

    private Object required_time;

    public Object getRequiredTime() { return this.required_time; }

    public void setRequiredTime(Object required_time) { this.required_time = required_time; }

    private boolean reservation;

    public boolean getReservation() { return this.reservation; }

    public void setReservation(boolean reservation) { this.reservation = reservation; }

    private Object route_address_entrance_from;

    public Object getRouteAddressEntranceFrom() { return this.route_address_entrance_from; }

    public void setRouteAddressEntranceFrom(Object route_address_entrance_from) { this.route_address_entrance_from = route_address_entrance_from; }

    private String comment;

    public String getComment() { return this.comment; }

    public void setComment(String comment) { this.comment = comment; }

    private double add_cost;

    public double getAddCost() { return this.add_cost; }

    public void setAddCost(double add_cost) { this.add_cost = add_cost; }

    private boolean wagon;

    public boolean getWagon() { return this.wagon; }

    public void setWagon(boolean wagon) { this.wagon = wagon; }

    private boolean minibus;

    public boolean getMinibus() { return this.minibus; }

    public void setMinibus(boolean minibus) { this.minibus = minibus; }

    private boolean premium;

    public boolean getPremium() { return this.premium; }

    public void setPremium(boolean premium) { this.premium = premium; }

    private String flexible_tariff_name;

    public String getFlexibleTariffName() { return this.flexible_tariff_name; }

    public void setFlexibleTariffName(String flexible_tariff_name) { this.flexible_tariff_name = flexible_tariff_name; }

    private boolean baggage;

    public boolean getBaggage() { return this.baggage; }

    public void setBaggage(boolean baggage) { this.baggage = baggage; }

    private boolean animal;

    public boolean getAnimal() { return this.animal; }

    public void setAnimal(boolean animal) { this.animal = animal; }

    private boolean conditioner;

    public boolean getConditioner() { return this.conditioner; }

    public void setConditioner(boolean conditioner) { this.conditioner = conditioner; }

    private boolean courier_delivery;

    public boolean getCourierDelivery() { return this.courier_delivery; }

    public void setCourierDelivery(boolean courier_delivery) { this.courier_delivery = courier_delivery; }

    private boolean route_undefined;

    public boolean getRouteUndefined() { return this.route_undefined; }

    public void setRouteUndefined(boolean route_undefined) { this.route_undefined = route_undefined; }

    private boolean terminal;

    public boolean getTerminal() { return this.terminal; }

    public void setTerminal(boolean terminal) { this.terminal = terminal; }

    private boolean receipt;

    public boolean getReceipt() { return this.receipt; }

    public void setReceipt(boolean receipt) { this.receipt = receipt; }

    private ArrayList<Route> route;

    public ArrayList<Route> getRoute() { return this.route; }

    public void setRoute(ArrayList<Route> route) { this.route = route; }

    private int taxiColumnId;

    public int getTaxiColumnId() { return this.taxiColumnId; }

    public void setTaxiColumnId(int taxiColumnId) { this.taxiColumnId = taxiColumnId; }

}

package com.taxi.clickcar.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Назар on 12.09.2016.
 */
public class AuthResponse {
    @SerializedName("id")
    public int id;
    @SerializedName("roles")
    public String roles;
    @SerializedName("version")
    public String version;
    @SerializedName("login")
    public String login;
    @SerializedName("user_full_name")
    public String userFullName;
    @SerializedName("user_first_name")
    public String userFirstName;
    @SerializedName("user_last_name")
    public String userLastName;
    @SerializedName("user_middle_name")
    public String userMiddleName;
    @SerializedName("user_phone")
    public String userPhone;
    @SerializedName("user_balance")
    public int userBalance;
    @SerializedName("route_address_from")
    public Object routeAddressFrom;
    @SerializedName("route_address_number_from")
    public Object routeAddressNumberFrom;
    @SerializedName("route_address_entrance_from")
    public Object routeAddressEntranceFrom;
    @SerializedName("route_address_apartment_from")
    public Object routeAddressApartmentFrom;
    @SerializedName("discount")
    public Discount discount;
    @SerializedName("client_bonuses")
    public Object clientBonuses;
    @SerializedName("client_sub_cards")
    public List<Object> clientSubCards = new ArrayList<Object>();
    @SerializedName("payment_type")
    public int paymentType;
    public AuthResponse() {
    }
    public AuthResponse(Integer id, String roles, String version, String login, String userFullName, String userFirstName, String userLastName, String userMiddleName, String userPhone, Integer userBalance, Object routeAddressFrom, Object routeAddressNumberFrom, Object routeAddressEntranceFrom, Object routeAddressApartmentFrom, Discount discount, Object clientBonuses, List<Object> clientSubCards, Integer paymentType) {
        this.id = id;
        this.roles = roles;
        this.version = version;
        this.login = login;
        this.userFullName = userFullName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userMiddleName = userMiddleName;
        this.userPhone = userPhone;
        this.userBalance = userBalance;
        this.routeAddressFrom = routeAddressFrom;
        this.routeAddressNumberFrom = routeAddressNumberFrom;
        this.routeAddressEntranceFrom = routeAddressEntranceFrom;
        this.routeAddressApartmentFrom = routeAddressApartmentFrom;
        this.discount = discount;
        this.clientBonuses = clientBonuses;
        this.clientSubCards = clientSubCards;
        this.paymentType = paymentType;
    }
    public String getUserFullName(){
        return userFullName;
    }
    public String getUserPhone(){
        return userPhone;
    }
public class Discount {

    public Integer value;
    public String unit;

    public Discount() {
    }

    public Discount(Integer value, String unit) {
        this.value = value;
        this.unit = unit;
    }
}
}
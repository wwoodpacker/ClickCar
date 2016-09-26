package com.taxi.clickcar.Order;

import java.util.ArrayList;

/**
 * Created by Назар on 04.06.2016.
 */
public class History {
    private String dispatching_order_uid;

    public String getDispatchingOrderUid() { return this.dispatching_order_uid; }

    public void setDispatchingOrderUid(String dispatching_order_uid) { this.dispatching_order_uid = dispatching_order_uid; }

    private String required_time;

    public String getRequiredTime() { return this.required_time; }

    public void setRequiredTime(String required_time) { this.required_time = required_time; }

    private double order_cost;

    public double getOrderCost() { return this.order_cost; }

    public void setOrderCost(double order_cost) { this.order_cost = order_cost; }

    private String user_full_name;

    public String getUserFullName() { return this.user_full_name; }

    public void setUserFullName(String user_full_name) { this.user_full_name = user_full_name; }

    private String user_phone;

    public String getUserPhone() { return this.user_phone; }

    public void setUserPhone(String user_phone) { this.user_phone = user_phone; }

    private int close_reason;

    public int getCloseReason() { return this.close_reason; }

    public void setCloseReason(int close_reason) { this.close_reason = close_reason; }

    private String execution_status;

    public String getExecutionStatus() { return this.execution_status; }

    public void setExecutionStatus(String execution_status) { this.execution_status = execution_status; }

    private ArrayList<Route> route;

    public ArrayList<Route> getRoute() { return this.route; }

    public void setRoute(ArrayList<Route> route) { this.route = route; }
}
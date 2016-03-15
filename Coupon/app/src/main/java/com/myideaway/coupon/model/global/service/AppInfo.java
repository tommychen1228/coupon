package com.myideaway.coupon.model.global.service;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午11:17
 * To change this template use File | Settings | File Templates.
 */
public class AppInfo {
    String customerServicePhone;
    String customerServiceEmail;
    String aboutInfo;

    public String getCustomerServicePhone() {
        return customerServicePhone;
    }

    public void setCustomerServicePhone(String customerServicePhone) {
        this.customerServicePhone = customerServicePhone;
    }

    public String getCustomerServiceEmail() {
        return customerServiceEmail;
    }

    public void setCustomerServiceEmail(String customerServiceEmail) {
        this.customerServiceEmail = customerServiceEmail;
    }

    public String getAboutInfo() {
        return aboutInfo;
    }

    public void setAboutInfo(String aboutInfo) {
        this.aboutInfo = aboutInfo;
    }
}

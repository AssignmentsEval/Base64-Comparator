package com.waes.base64comp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Base64Data class is the entity class of Base64Data table.
 */
@Entity
@Table
public class Base64Data {

    /**
     * The primary key of Base64Data table.
     */
    @Id
    @Column
    private int id;

    /**
     * The left part of data.
     */
    @Column
    private String left_data;

    /**
     * The right part of data.
     */
    @Column
    private String right_data;

    public Base64Data() {

    }

    public Base64Data(int id, String left_data, String right_data) {
        this.id = id;
        this.left_data = left_data;
        this.right_data = right_data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeft_data() {
        return left_data;
    }

    public void setLeft_data(String left_data) {
        this.left_data = left_data;
    }

    public String getRight_data() {
        return right_data;
    }

    public void setRight_data(String right_data) {
        this.right_data = right_data;
    }

    @Override
    public String toString() {
        return "Base64Data{" +
                "id=" + id +
                ", left_data='" + left_data + '\'' +
                ", right_data='" + right_data + '\'' +
                '}';
    }
}

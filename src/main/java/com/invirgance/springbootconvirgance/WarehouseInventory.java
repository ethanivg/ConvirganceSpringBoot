package com.invirgance.springbootconvirgance;

import com.invirgance.convirgance.json.JSONObject;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WAREHOUSE_INVENTORY")
@IdClass(WarehouseInventoryId.class)
public class WarehouseInventory
{

    @Id
    @Column(name = "WAREHOUSE_ID")
    private Integer warehouseId;

    @Id
    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "BIN_DATE")
    private LocalDateTime binDate;

    @Column(name = "QUANTITY")
    private Integer quantity;

    public WarehouseInventory()
    {
    }

    public WarehouseInventory(JSONObject item)
    {
        this.setItem(item);
    }

    public final void setItem(JSONObject item)
    {
        this.setWarehouseId(item.getInt("WAREHOUSE_ID"));
        this.setProductId(item.getInt("PRODUCT_ID"));
        this.setManufacturer(item.getString("MANUFACTURER"));
        this.setProduct(item.getString("PRODUCT"));

        String binDateStr = item.getString("BIN_DATE");
        
        if (binDateStr != null) this.setBinDate(LocalDateTime.parse(binDateStr));

        this.setQuantity(item.getInt("QUANTITY"));
    }

    public Integer getWarehouseId()
    {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId)
    {
        this.warehouseId = warehouseId;
    }

    public Integer getProductId()
    {
        return productId;
    }

    public void setProductId(Integer productId)
    {
        this.productId = productId;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public LocalDateTime getBinDate()
    {
        return binDate;
    }

    @Override
    public String toString()
    {
        return "{"
                + "\"WAREHOUSE_ID\":" + warehouseId
                + ", \"PRODUCT_ID\":" + productId
                + ", \"MANUFACTURER\":\"" + manufacturer + "\""
                + ", \"PRODUCT\":\"" + product + "\""
                + ", \"BIN_DATE\":\"" + binDate + "\""
                + ", \"QUANTITY\":" + quantity
                + "}";
    }

    public void setBinDate(LocalDateTime binDate)
    {
        this.binDate = binDate;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }
}

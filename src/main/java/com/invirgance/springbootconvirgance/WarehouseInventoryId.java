/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invirgance.springbootconvirgance;

import java.io.Serializable;
import java.util.Objects;

/**
 * An aggregate key for WarehouseInventory Objects.
 * @author tadghh
 */
public class WarehouseInventoryId implements Serializable
{

    private Integer warehouseId;
    private Integer productId;

    public WarehouseInventoryId()
    {
    }

    public WarehouseInventoryId(Integer warehouseId, Integer productId)
    {
        this.warehouseId = warehouseId;
        this.productId = productId;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarehouseInventoryId that = (WarehouseInventoryId) o;
        
        return Objects.equals(warehouseId, that.warehouseId)
                && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(warehouseId, productId);
    }
}

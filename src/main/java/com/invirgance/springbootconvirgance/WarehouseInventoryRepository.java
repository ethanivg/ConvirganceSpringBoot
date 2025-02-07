/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invirgance.springbootconvirgance;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author tadghh
 */
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, WarehouseInventoryId>
{
}

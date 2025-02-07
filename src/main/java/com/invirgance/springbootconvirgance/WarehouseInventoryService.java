package com.invirgance.springbootconvirgance;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarehouseInventoryService
{

    private static final Logger logger = LoggerFactory.getLogger(WarehouseInventoryService.class);
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    @Autowired
    public WarehouseInventoryService(WarehouseInventoryRepository warehouseInventoryRepository)
    {
        this.warehouseInventoryRepository = warehouseInventoryRepository;
    }

    @Transactional
    public void saveInventory(WarehouseInventory inventory)
    {
        try
        {
            warehouseInventoryRepository.save(inventory);
            logger.info("Saved inventory item for warehouse: {}, product: {}",
                    inventory.getWarehouseId(), inventory.getProduct());
        }
        catch (Exception e)
        {
            logger.error("Error saving inventory item", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<WarehouseInventory> getAllInventory()
    {
        try
        {
            logger.info("Fetching all inventory items");
            List<WarehouseInventory> items = warehouseInventoryRepository.findAll();

            return items;
        }
        catch (Exception e)
        {
            logger.error("Error fetching inventory items", e);
            throw e;
        }
    }

}

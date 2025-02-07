package com.invirgance.springbootconvirgance;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  // Add this import
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WarehouseViewController
{

    private static final Logger logger = LoggerFactory.getLogger(WarehouseViewController.class);
    private final WarehouseInventoryService warehouseInventoryService;

    @Autowired
    public WarehouseViewController(WarehouseInventoryService warehouseInventoryService)
    {
        this.warehouseInventoryService = warehouseInventoryService;
    }

    @GetMapping("/")
    public String index(Model model)
    {
        try
        {
            var items = warehouseInventoryService.getAllInventory();

            if (items == null) items = Collections.emptyList();

            model.addAttribute("inventoryItems", items);
            return "index";
        }
        catch (Exception e)
        {
            logger.error("Error in index controller", e);
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }
}

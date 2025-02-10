package com.invirgance.springbootconvirgance;

import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.dbms.QueryOperation;
import com.invirgance.convirgance.json.JSONObject;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  // Add this import
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WarehouseViewController
{

    private final DBMS dbms;

    @Autowired
    public WarehouseViewController(DataSource dataSource)
    {
        this.dbms = new DBMS(dataSource);
    }
    
    @GetMapping("/")
    public String index(Model model)
    {
        model.addAttribute("inventoryItems", this.getAllInventory());
        return "index";
    }
    
    public Iterable<JSONObject> getAllInventory()
    {
        return dbms.query(new Query("SELECT * FROM WAREHOUSE_INVENTORY"));
    }
    
    public void saveInventory(JSONObject inventory)
    {
        String sql = "insert into WAREHOUSE_INVENTORY values (:WAREHOUSE_ID, :PRODUCT_ID, :MANUFACTURER, :PRODUCT, :BIN_DATE, :QUANTITY)";
        Query insert = new Query(sql);
        QueryOperation operation = new QueryOperation(insert);

        insert.setBindings(inventory);
        dbms.update(operation);
    }
}

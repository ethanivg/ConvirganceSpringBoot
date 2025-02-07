package com.invirgance.springbootconvirgance;

import com.invirgance.convirgance.dbms.BatchOperation;
import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.dbms.QueryOperation;
import com.invirgance.convirgance.json.JSONObject;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseInventoryService
{

    private final DBMS dbms;

    @Autowired
    public WarehouseInventoryService(DataSource dataSource)
    {
        this.dbms = new DBMS(dataSource);
    }

    public void saveInventory(JSONObject inventory)
    {
        try
        {
            String sql = "insert into WAREHOUSE_INVENTORY values (:WAREHOUSE_ID, :PRODUCT_ID, :MANUFACTURER, :PRODUCT, :BIN_DATE, :QUANTITY)";
            Query insert = new Query(sql);
            QueryOperation operation = new QueryOperation(insert);

            insert.setBindings(inventory);
            dbms.update(operation);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error saving inventory", e);
        }
    }

    public void saveInventoryBulk(Iterable<JSONObject> inventory)
    {
        try
        {
            String sql = "insert into WAREHOUSE_INVENTORY values (:WAREHOUSE_ID, :PRODUCT_ID, :MANUFACTURER, :PRODUCT, :BIN_DATE, :QUANTITY)";
            Query insert = new Query(sql);

            dbms.update(new BatchOperation(insert, inventory));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error saving inventory", e);
        }
    }

    public Iterable<JSONObject> getAllInventory()
    {
        try
        {
            return dbms.query(new Query("SELECT * FROM WAREHOUSE_INVENTORY"));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error fetching inventory", e);
        }
    }
 
}

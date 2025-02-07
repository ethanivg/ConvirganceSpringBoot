package com.invirgance.springbootconvirgance;

import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.dbms.QueryOperation;
import com.invirgance.convirgance.input.JSONInput;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.source.FileSource;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer
{

    private static final Logger logger = LoggerFactory.getLogger(WarehouseInventoryService.class);

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initializeDatabase()
    {
        try
        {
            DBMS dbms = new DBMS(dataSource);

            dbms.update(new QueryOperation(new Query("DROP TABLE IF EXISTS WAREHOUSE_INVENTORY;")));
            dbms.update(new QueryOperation(new Query("CREATE TABLE WAREHOUSE_INVENTORY (\n"
                    + "WAREHOUSE_ID INTEGER,\n"
                    + "PRODUCT_ID INTEGER,\n"
                    + "MANUFACTURER VARCHAR(255),\n"
                    + "PRODUCT VARCHAR(255),\n"
                    + "BIN_DATE TIMESTAMP,\n"
                    + "QUANTITY INTEGER\n"
                    + ")")));

            for (JSONObject record : new JSONInput().read(new FileSource("src/test/resources/inventory_seed.json")))
            {
                dbms.update(new QueryOperation(new Query("insert into WAREHOUSE_INVENTORY values (:WAREHOUSE_ID, :PRODUCT_ID, :MANUFACTURER, :PRODUCT, :BIN_DATE, :QUANTITY)", record)));
            }
        }
        catch (Exception e)
        {
            logger.error("Database Initialization failed, is the database running?");
        }
    }
}

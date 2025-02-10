package com.invirgance.springbootconvirgance;

import com.invirgance.convirgance.dbms.BatchOperation;
import com.invirgance.convirgance.dbms.DBMS;
import com.invirgance.convirgance.dbms.Query;
import com.invirgance.convirgance.dbms.QueryOperation;
import com.invirgance.convirgance.dbms.TransactionOperation;
import com.invirgance.convirgance.input.JSONInput;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.source.FileSource;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer
{

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initializeDatabase()
    {
        QueryOperation reset = new QueryOperation(new Query("DROP TABLE IF EXISTS WAREHOUSE_INVENTORY;"));
        QueryOperation create = new QueryOperation(new Query("CREATE TABLE WAREHOUSE_INVENTORY (\n"
                + "WAREHOUSE_ID INTEGER,\n"
                + "PRODUCT_ID INTEGER,\n"
                + "MANUFACTURER VARCHAR(255),\n"
                + "PRODUCT VARCHAR(255),\n"                    
                + "BIN_DATE VARCHAR(255),\n"                    
                + "QUANTITY INTEGER\n"
                + ")"));

        FileSource seed = new FileSource("src/test/resources/inventory_seed.json");
        Iterable<JSONObject> stream = new JSONInput().read(seed);
        
        String sql = "insert into WAREHOUSE_INVENTORY values (:WAREHOUSE_ID, :PRODUCT_ID, :MANUFACTURER, :PRODUCT, :BIN_DATE, :QUANTITY)";
        Query insert = new Query(sql);
        
        BatchOperation populate = new BatchOperation(insert, stream);

        TransactionOperation setup = new TransactionOperation(reset, create, populate);
        
        DBMS dbms = new DBMS(dataSource);
        dbms.update(setup);
    }
}

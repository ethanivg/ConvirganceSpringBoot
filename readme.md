# Convirgance - SpringBoot

# Setup
- Start a PSQL server, or configure your own in application.properties
	- Default user is set to `postgres`
- Create a database called `test_warehouse`
- Whenever the project is run the `WAREHOUSE_INVENTORY` table will be dropped and re-seeded
	- See `DatabaseInitializer.java` for more info
	
# Note
Usage of Convirgance is primarly located inside `WarehouseController.java`

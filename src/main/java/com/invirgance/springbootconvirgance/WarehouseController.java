package com.invirgance.springbootconvirgance;

import com.invirgance.convirgance.input.CSVInput;
import com.invirgance.convirgance.input.DelimitedInput;
import com.invirgance.convirgance.input.Input;
import com.invirgance.convirgance.input.JSONInput;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.output.CSVOutput;
import com.invirgance.convirgance.output.DelimitedOutput;
import com.invirgance.convirgance.output.JSONOutput;
import com.invirgance.convirgance.output.Output;
import com.invirgance.convirgance.output.OutputCursor;
import com.invirgance.convirgance.source.InputStreamSource;
import com.invirgance.convirgance.target.OutputStreamTarget;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * The WarehouseController controls the behaviour for different routes.
 * @author tadghh
 */
@RestController
@RequestMapping("/api/inventory")
public class WarehouseController
{

    private static final Logger logger = LoggerFactory.getLogger(WarehouseInventoryService.class);

    @Autowired
    private WarehouseInventoryService warehouseInventoryService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadInventory(@RequestBody WarehouseInventory inventory)
    {
        try
        {       
            warehouseInventoryService.saveInventory(inventory);
            return ResponseEntity.ok("Inventory record stored successfully!");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body("Error storing inventory record: " + e.getMessage());
        }
    }
    
    /**
     * Returns the inventory objects currently stored in the WarehouseService's repository
     * @return A list of Inventory Objects.
     */
    @GetMapping("/all")
    public ResponseEntity<List<WarehouseInventory>> getAllInventory()
    {
        return ResponseEntity.ok(warehouseInventoryService.getAllInventory());
    }

    /**
     * Converts the currently uploaded file into the requested format. 
     * Both the download and upload are streamed at the same time.
     * @param file the uploaded file to be converted.
     * @param outputFormat The file-type to convert the uploaded file to.
     * @return The stream containing the new converted file.
     */
    @PostMapping("/convert")
    public ResponseEntity<StreamingResponseBody> convertFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("outputFormat") String outputFormat)
    {
        try
        {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
            String filename = String.format("converted.%s", outputFormat);

            StreamingResponseBody responseBody;

            Input input = switch (extension)
            {
                case "csv" ->
                    new CSVInput();
                case "json" ->
                    new JSONInput();
                case "txt" ->
                    new DelimitedInput();
                default ->
                    null;
            };

            Output output = switch (outputFormat)
            {
                case "json" ->
                    new JSONOutput();
                case "csv" ->
                    new CSVOutput();
                case "pipe" ->
                    new DelimitedOutput();
                default ->
                    null;
            };

            if (input == null || output == null) return ResponseEntity.badRequest().build();

            responseBody = outputStream ->
            {
                try (OutputCursor cursor = output.write(new OutputStreamTarget(outputStream)))
                {
                    var content = input.read(new InputStreamSource(file.getInputStream())).iterator();

                    while (content.hasNext()) cursor.write(new JSONObject(content.next().toString()));
                }
                catch (Exception e)
                {
                    logger.error("Error streaming download", e);
                    throw new RuntimeException("Error streaming download", e);
                }
            };

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, output.getContentType())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(responseBody);

        }
        catch (Exception e)
        {
            logger.error("Error in file conversion", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Takes in a upload file, adding its contents to the WarehouseServices' repository.
     * @param file The uploaded file.
     * @return A string with the status of the upload.
     */
    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file)
    {
        try
        {
            String filename = file.getOriginalFilename();
            String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

            Input input = switch (extension)
            {
                case "csv" ->
                {
                    yield new CSVInput();
                }
                case "json" ->
                {
                    yield new JSONInput();
                }
                case "txt" ->
                {
                    yield new DelimitedInput();
                }
                default ->
                {
                    yield null;
                }
            };

            if (input != null)
            {
                var content = input.read(new InputStreamSource(file.getInputStream())).iterator();

                while (content.hasNext()) warehouseInventoryService.saveInventory(new WarehouseInventory(new JSONObject(content.next().toString())));
            }
            else
            {
                logger.info("Unsupported File.");
                return ResponseEntity.badRequest().body("Unsupported file type");
            }

            return ResponseEntity.ok("File processed successfully");
        }
        catch (Exception e)
        {
            logger.info("Error uploading: {}", e);
            return ResponseEntity.status(500)
                    .body("Error processing file: " + e.getMessage());
        }
    }

    /**
     * Creates a download with the current contents of the WarehouseServices' inventory repository.
     * @param format The requested download file-type format.
     * @return A stream of the inventory repository.
     */
    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadInventory(@RequestParam String format)
    {
        StreamingResponseBody responseBody;
        Iterator<WarehouseInventory> items = warehouseInventoryService.getAllInventory().iterator();
        String filename;

        Output output = switch (format.toLowerCase())
        {
            case "json" ->
            {

                filename = "export.json";
                yield new JSONOutput();
            }

            case "csv" ->
            {

                filename = "export.csv";
                yield new CSVOutput();
            }

            case "pipe" ->
            {

                filename = "export.txt";
                yield new DelimitedOutput();
            }

            default ->
            {
                filename = "";
                yield null;
            }
        };

        if (output == null) return ResponseEntity.badRequest().build();

        responseBody = outputStream ->
        {
            try (OutputCursor cursor = output.write(new OutputStreamTarget(outputStream)))
            {
                while (items.hasNext()) cursor.write(new JSONObject(items.next().toString()));
            }
            catch (Exception e)
            {
                logger.error("Error streaming download", e);
                throw new RuntimeException("Error streaming download", e);
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(output.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(responseBody);
    }

}

package com.invirgance.springbootconvirgance;

import com.invirgance.convirgance.input.CSVInput;
import com.invirgance.convirgance.input.DelimitedInput;
import com.invirgance.convirgance.input.Input;
import com.invirgance.convirgance.input.InputCursor;
import com.invirgance.convirgance.input.JSONInput;
import com.invirgance.convirgance.json.JSONObject;
import com.invirgance.convirgance.output.CSVOutput;
import com.invirgance.convirgance.output.DelimitedOutput;
import com.invirgance.convirgance.output.JSONOutput;
import com.invirgance.convirgance.output.Output;
import com.invirgance.convirgance.source.InputStreamSource;
import com.invirgance.convirgance.target.OutputStreamTarget;
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
 * The WarehouseController controls the behavior for different routes.
 * @author tadghh
 */
@RestController
@RequestMapping("/api/inventory")
public class WarehouseController
{

    @Autowired
    private WarehouseViewController warehouseInventoryService;
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadInventory(@RequestBody JSONObject inventory)
    {
        warehouseInventoryService.saveInventory(inventory);
        return ResponseEntity.ok("Inventory record stored successfully!");
    }
    
    /**
     * Creates a download with the current contents of the WarehouseServices' inventory repository.
     * 
     * @param format The requested download file-type format.
     * @return A stream of the inventory repository.
     */
    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadInventory(@RequestParam String format)
    {
        StreamingResponseBody responseBody;
        Iterable<JSONObject> items = warehouseInventoryService.getAllInventory();
        
        String filename;
        String type = format.toLowerCase();
        
        Output output = getOutputObject(type);
        
        filename = "input." + type;

        responseBody = outputStream -> {
            output.write(new OutputStreamTarget(outputStream), items);
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(output.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(responseBody);
    }

    /**
     * Converts the currently uploaded file into the requested format. 
     * Both the download and upload are streamed at the same time.
     * 
     * @param file the uploaded file to be converted.
     * @param outputFormat The file-type to convert the uploaded file to.
     * @return The stream containing the new converted file.
     */
    @PostMapping("/convert")
    public ResponseEntity<StreamingResponseBody> convertFile(@RequestParam("file") MultipartFile file, @RequestParam("outputFormat") String outputFormat)
    {
     
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
        String filename = String.format("converted.%s", outputFormat);

        StreamingResponseBody responseBody;

        Input<JSONObject> input = getInputObject(extension);

        Output output = getOutputObject(outputFormat);

        responseBody = outputStream -> {
            InputCursor<JSONObject> content = input.read(new InputStreamSource(file.getInputStream()));
            output.write(new OutputStreamTarget(outputStream), content);
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, output.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(responseBody);
    }    
    
    /**
     * Returns the input object for the given format.
     * 
     * @param format A string representing a file extension.
     * @return The `Input` for the provided file type or null.
     */
    private Input<JSONObject> getInputObject(String format)
    {
        return switch (format) 
        {
            case "json" -> new JSONInput();
            case "csv" -> new CSVInput();
            case "txt" -> new DelimitedInput();
            default -> null;
        };
    }
       
    /**
     * Returns the output object for the provided file-type.
     * 
     * @param format The file type.
     * @return The relevant Output object, or null 
     */
    private Output getOutputObject(String format)
    {
        return switch (format) 
        {
            case "json" -> new JSONOutput();
            case "csv" -> new CSVOutput();
            case "txt" -> new DelimitedOutput();
            default -> null;
        };
    }
}

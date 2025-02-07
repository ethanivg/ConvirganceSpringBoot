yieldUnescaped '<!DOCTYPE html>'
html(lang:'en') {
    head {
        meta('charset':'utf-8')
        meta('name':'viewport', content:'width=device-width, initial-scale=1')
        title('Warehouse Inventory Management')
        link(href: 'https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css', rel: 'stylesheet')
        link(href: 'https://cdn.datatables.net/1.11.5/css/dataTables.bootstrap5.min.css', rel: 'stylesheet')
    }
    body {
        div(class: 'container mt-5') {
            h1('Warehouse Inventory Management')
            if (binding != null && binding.hasVariable('inventoryItems')) {
                div(class: 'alert alert-info') {
                    p("Debug: ${inventoryItems?.size() ?: 0} items found")
                }
            }

            div(class: ' mt-4 d-flex flex-nowrap') {
				// Inventory Table Section
				div(class: 'card-body w-75') {
					div(class: 'card-body') {
						h5(class: 'card-title mb-4', 'Current Inventory')
						div(class: 'table-responsive') {
							table(id: 'inventoryTable', class: 'table table-striped') {
								thead {
									tr {
										th('Warehouse ID')
										th('Product ID')
										th('Manufacturer')
										th('Product')
										th('Bin Date')
										th('Quantity')
									}
								}
								tbody {
									inventoryItems.each { item ->
										tr {
											td(item.warehouse_id)
											td(item.product_id)
											td(item.manufacturer)
											td(item.product)
											td(item.bin_date)
											td(item.quantity)
										}
									}
								}
							}
						}
					}
				}
                div(class: 'card-body d-flex w-25 flex-grow-1') {
					div(class: 'card-body d-flex flex-column justify-content-between flex-grow-1') {
						h5(class: 'card-title mb-auto', 'Download Inventory Data')
						form(id: 'downloadForm', class: 'd-flex flex-column mt-auto') {
							div(class: 'mb-3') {
								p(class: 'mb-2', 'Select Format:')
								div(class: 'form-check') {
									input(type: 'radio', class: 'form-check-input', id: 'formatJSON', name: 'format', value: 'json', checked: '')
									label(class: 'form-check-label', for: 'formatJSON', 'JSON')
								}
								div(class: 'form-check') {
									input(type: 'radio', class: 'form-check-input', id: 'formatCSV', name: 'format', value: 'csv')
									label(class: 'form-check-label', for: 'formatCSV', 'CSV')
								}
								div(class: 'form-check') {
									input(type: 'radio', class: 'form-check-input', id: 'formatPipe', name: 'format', value: 'txt')
									label(class: 'form-check-label', for: 'formatPipe', 'Pipe Delimited')
								}
							}
							button(type: 'submit', class: 'btn btn-secondary', 'Download Data')
						}
					}
                }
            }
			
	        div(class: ' mt-4 d-flex flex-nowrap justify-content-between') {
				// Bulk Upload Section
				div(class: 'card mt-4 mr-3 ', style: 'width: 48%') {
					div(class: 'card-body') {
						h5(class: 'card-title', 'Bulk Upload Inventory')
						div(class: 'alert alert-info') {
							p('Supported formats: CSV, JSON, Delimited(txt)')
							p('Expected CSV format:\n WAREHOUSE_ID, PRODUCT_ID, MANUFACTURER, PRODUCT, BIN_DATE, QUANTITY')
							p('Expected JSON format:\n {"WAREHOUSE_ID": 1, "PRODUCT_ID": 101, "MANUFACTURER": "ABC Corp", "PRODUCT": "Widget A", "BIN_DATE": "2025-02-06T10:00", "QUANTITY": 100}')
						}
						form(id: 'uploadForm', class: 'mt-3') {
							div(class: 'mb-3') {
								label(for: 'fileUpload', class: 'form-label', 'Choose File')
								input(type: 'file', class: 'form-control', id: 'fileUpload', accept: '.csv,.json,.txt', required: '')
							}
							button(type: 'submit', class: 'btn btn-primary', 'Upload File')
						}
					}
				}

				// Format Conversion Section
				div(class: 'card mt-4 ml-3 ', style: 'width: 48%') {
					div(class: 'card-body') {
						h5(class: 'card-title', 'Convert File Format')
						div(class: 'alert alert-info') {
							p('Supported input formats: CSV, JSON, Pipe Delimited (txt)')
							p('Select your desired output format and upload a file to convert')
						}
						form(id: 'conversionForm', class: 'mt-3') {
							div(class: 'mb-3') {
								label(class: 'form-label', 'Convert To:')
								div(class: 'mb-2') {
									div(class: 'form-check') {
										input(type: 'radio', class: 'form-check-input', id: 'outputJSON', name: 'outputFormat', value: 'json')
										label(class: 'form-check-label', for: 'outputJSON', 'JSON')
									}
									div(class: 'form-check') {
										input(type: 'radio', class: 'form-check-input', id: 'outputCSV', name: 'outputFormat', value: 'csv', checked: '')
										label(class: 'form-check-label', for: 'outputCSV', 'CSV')
									}
									div(class: 'form-check') {
										input(type: 'radio', class: 'form-check-input', id: 'outputPipe', name: 'outputFormat', value: 'txt')
										label(class: 'form-check-label', for: 'outputPipe', 'Pipe Delimited')
									}
								}
							}
							div(class: 'mb-3') {
								label(for: 'convertFile', class: 'form-label', 'Choose File')
								input(type: 'file', class: 'form-control', id: 'convertFile', accept: '.csv,.json,.txt', required: '')
							}
							button(type: 'submit', class: 'btn btn-primary', 'Convert File')
						}
					}
				}
				
		    }
           
            // Input Form Section
            div(class: 'card mt-4') {
                div(class: 'card-body') {
                    h5(class: 'card-title', 'Add New Inventory Item')
                    form(id: 'inventoryForm', class: 'mt-3') {
                        div(class: 'row mb-3') {
                            div(class: 'col-md-6') {
                                label(for: 'warehouse_id', class: 'form-label', 'Warehouse ID')
                                input(type: 'number', class: 'form-control', id: 'warehouse_id', name: 'warehouse_id', required: '')
                            }
                            div(class: 'col-md-6') {
                                label(for: 'product_id', class: 'form-label', 'Product ID')
                                input(type: 'number', class: 'form-control', id: 'product_id', name: 'product_id', required: '')
                            }
                        }
                        div(class: 'row mb-3') {
                            div(class: 'col-md-6') {
                                label(for: 'manufacturer', class: 'form-label', 'Manufacturer')
                                input(type: 'text', class: 'form-control', id: 'manufacturer', name: 'manufacturer', required: '')
                            }
                            div(class: 'col-md-6') {
                                label(for: 'product', class: 'form-label', 'Product')
                                input(type: 'text', class: 'form-control', id: 'product', name: 'product', required: '')
                            }
                        }
                        div(class: 'row mb-3') {
                            div(class: 'col-md-6') {
                                label(for: 'bin_date', class: 'form-label', 'Bin Date')
                                input(type: 'date', class: 'form-control', id: 'bin_date', name: 'bin_date', required: '')
                            }
                            div(class: 'col-md-6') {
                                label(for: 'quantity', class: 'form-label', 'Quantity')
                                input(type: 'number', class: 'form-control', id: 'quantity', name: 'quantity', required: '')
                            }
                        }
                        button(type: 'submit', class: 'btn btn-primary mt-3', 'Add Inventory Item')
                    }
                }
            }
            
           
        }
        
        // Scripts
        script(src: 'https://code.jquery.com/jquery-3.6.0.min.js') {}
        script(src: 'https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js') {}
        script(src: 'https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js') {}
        script(src: 'https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js') {}
        
        script {
            yieldUnescaped '''
                \$(document).ready(function() {
                    \$('#inventoryTable').DataTable({
                        pageLength: 10,
                        order: [[0, 'desc']],
                        language: {
                            search: "Search inventory:"
                        }
                    });
                });

                // Download handler
                document.getElementById('downloadForm').addEventListener('submit', function(e) {
                    e.preventDefault();
                    const format = document.querySelector('input[name="format"]:checked').value;
                    window.location.href = `/api/inventory/download?format=${format}`;
                });

                // File upload handler
                document.getElementById('uploadForm').addEventListener('submit', async function(e) {
                    e.preventDefault();
                    const fileInput = document.getElementById('fileUpload');
                    const file = fileInput.files[0];
                    if (!file) {
                        alert('Please select a file');
                        return;
                    }

                    const formData = new FormData();
                    formData.append('file', file);

                    try {
                        const response = await fetch('/api/inventory/upload-file', {
                            method: 'POST',
                            body: formData
                        });

                        if (!response.ok) {
                            throw new Error('Upload failed');
                        }

                        const result = await response.text();
                        alert('File uploaded successfully!');
                        window.location.reload();
                    } catch (error) {
                        alert('Error uploading file: ' + error.message);
                    }
                });

                // Format conversion handler
                document.getElementById('conversionForm').addEventListener('submit', async function(e) {
                    e.preventDefault();
                    
                    const formData = new FormData();
                    const file = document.getElementById('convertFile').files[0];
                    const outputFormat = document.querySelector('input[name="outputFormat"]:checked').value;
                    
                    if (!file) {
                        alert('Please select a file');
                        return;
                    }
                    
                    formData.append('file', file);
                    formData.append('outputFormat', outputFormat);
                    
                    try {
                        const response = await fetch('/api/inventory/convert', {
                            method: 'POST',
                            body: formData
                        });
                        
                        if (!response.ok) {
                            throw new Error('Conversion failed');
                        }
                        
                        // Grab the filename from the header.
                        const contentDisposition = response.headers.get('Content-Disposition');
                        const filename = contentDisposition 
                            ? contentDisposition.split('filename=')[1].replace(/"/g, '')
                            : `converted.${outputFormat}`;
                            
                        // Create a download from the response stream.
                        const blob = await response.blob();
                        const url = window.URL.createObjectURL(blob);
                        const a = document.createElement('a');
                        a.href = url;
                        a.download = filename;
                        document.body.appendChild(a);
                        a.click();
                        window.URL.revokeObjectURL(url);
                        document.body.removeChild(a);
                    } catch (error) {
                        alert('Error converting file: ' + error);
                    }
                });

                // Single item form handler
                document.getElementById('inventoryForm').addEventListener('submit', function(e) {
                    e.preventDefault();
					
					// We don't allow the hours to be set.
                    const binDateValue = document.getElementById('bin_date').value;
					const binDateTime = binDateValue + ' 14:00:00'; 

                    const formData = {
                        warehouse_id: parseInt(document.getElementById('warehouse_id').value),
                        product_id: parseInt(document.getElementById('product_id').value),
                        manufacturer: document.getElementById('manufacturer').value,
                        product: document.getElementById('product').value,
                        bin_date: binDateTime,
                        quantity: parseInt(document.getElementById('quantity').value)
                    };
                    
                    fetch('/api/inventory/upload', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(formData)
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.text();
                    })
                    .then(data => {
                        alert('Inventory item added successfully!');
                        document.getElementById('inventoryForm').reset();
                        window.location.reload();
                    })
                    .catch((error) => {
                        alert('Error adding inventory item: ' + error);
                    });
                });
            '''
        }
    }
}
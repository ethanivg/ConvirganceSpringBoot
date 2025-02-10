yieldUnescaped '<!DOCTYPE html>'
html(lang:'en') {
    head {
        meta('charset':'utf-8')
        meta('name':'viewport', content:'width=device-width, initial-scale=1')
        title('Warehouse Inventory Management')
		
		// Stylesheets
		link(href: 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css', rel: 'stylesheet')
		link(href: 'https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-2.2.2/datatables.min.css', rel: 'stylesheet')
		
		// Scripts
		script(src: 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js') {}
		script(src: 'https://code.jquery.com/jquery-3.7.1.min.js') {}
		script(src: 'https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-2.2.2/datatables.min.js') {}
    }
    body {
        div(class: 'container mt-5') {
            h1('Warehouse Inventory Management')

            div(class: 'mt-4 d-flex flex-column  flex-nowrap') {
				// Inventory Table Section
				div(class: 'card-body pb-0') {
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
                div(class: 'card-body pt-0 d-flex w-25') {
					div(class: 'card-body d-flex flex-column flex-grow-1') {
						h5(class: 'card-title', 'Download Inventory Data')
						form(id: 'downloadForm') {
							button(type: 'submit', class: 'btn btn-primary', 'Download Data (CSV)')
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
			
		    // Format Conversion Section			
	        div(class: 'mt-4') {
				div(class: 'card mt-4 ml-3 ') {
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
        }
        
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
                    window.location.href = `/api/inventory/download?format=csv`;
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
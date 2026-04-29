// =====================================================
// JOB CONTRACT FORM - JavaScript Module
// =====================================================

// Set today's date
document.addEventListener("DOMContentLoaded", function () {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    document.getElementById("contractDate").value = `${year}-${month}-${day}`;
});

// DOM Elements
const form = document.getElementById("billForm");
const loader = document.getElementById("loadingOverlay");
const submitBtn = document.getElementById("generateContractBtn"); // Changed from submitBtn to match HTML ID

// Initialize Select2 and event listeners
$(document).ready(function() {
    $('.searchable').select2({
        placeholder: "Search here...",
        allowClear: true
    });

    // Auto-fill Weaver Brokerage on weaver selection
    $('#weaverSelect').on('select2:select', function(e) {
        const weaverName = $(this).val();
        console.log("Weaver selected:", weaverName);
        if (weaverName) {
            const url = `/api/weaver-details/${encodeURIComponent(weaverName)}`;
            console.log("Fetching from:", url);
            fetch(url)
                .then(res => {
                    console.log("Response status:", res.status);
                    return res.json();
                })
                .then(data => {
                    console.log("Weaver data:", data);
                    document.getElementById("weaverBrokeragePercent").value = data.brokeragePercent || 0;
                    document.getElementById("weaverBrokeragePaisa").value = data.brokeragePaisa || 0;
                    calculateAll();
                })
                .catch(err => console.error("Error fetching weaver details:", err));
        }
    });

    // Auto-fill Pick from Quality
    $('#qualitySelect').on('select2:select', function(e) {
        const qualityName = $(this).val();
        console.log("Quality selected:", qualityName);
        if (qualityName) {
            const url = `/api/quality-details?qualityName=${encodeURIComponent(qualityName)}`;
            console.log("Fetching from:", url);
            fetch(url)
                .then(res => {
                    console.log("Response status:", res.status);
                    if (!res.ok) {
                        throw new Error(`HTTP ${res.status}: Failed to fetch quality details`);
                    }
                    return res.json();
                })
                .then(data => {
                    console.log("✓ Quality data received:", data);
                    
                    // Handle pick value (might be string or number)
                    const pickValue = data.pick ? String(data.pick).trim() : "0";
                    document.getElementById("pick").value = pickValue || "0";
                    
                    console.log("✓ Pick value set to:", pickValue);
                    
                    // Update other quality fields if they exist in the DOM
                    const fieldsToUpdate = {
                        "width": data.width,
                        "reed": data.reed,
                        "warp": data.warp,
                        "weft": data.weft,
                        "weave": data.weave
                    };
                    
                    for (const [fieldId, value] of Object.entries(fieldsToUpdate)) {
                        const element = document.getElementById(fieldId);
                        if (element && value) {
                            element.value = value;
                        }
                    }
                    
                    // Recalculate with the new pick value
                    calculateAll();
                    
                    if (data.error) {
                        console.warn("Warning from API:", data.error);
                    }
                })
                .catch(err => {
                    console.error("✗ Error fetching quality details:", err);
                    Swal.fire("Warning", "Could not fetch quality details: " + err.message, "warning");
                });
        }
    });
});

// Listen for changes to recalculate
const jobRateInput = document.getElementById("jobRate");
const quantityMetersInput = document.getElementById("quantityMeters");
const pickInput = document.getElementById("pick");
const brokeragePercentInput = document.getElementById("weaverBrokeragePercent");
const brokeragePaisaInput = document.getElementById("weaverBrokeragePaisa");

if (jobRateInput) jobRateInput.addEventListener("change", calculateAll);
if (jobRateInput) jobRateInput.addEventListener("input", calculateAll);
if (quantityMetersInput) quantityMetersInput.addEventListener("change", calculateAll);
if (quantityMetersInput) quantityMetersInput.addEventListener("input", calculateAll);
if (pickInput) pickInput.addEventListener("change", calculateAll);
if (pickInput) pickInput.addEventListener("input", calculateAll);
if (brokeragePercentInput) brokeragePercentInput.addEventListener("change", calculateAll);
if (brokeragePercentInput) brokeragePercentInput.addEventListener("input", calculateAll);
if (brokeragePaisaInput) brokeragePaisaInput.addEventListener("change", calculateAll);
if (brokeragePaisaInput) brokeragePaisaInput.addEventListener("input", calculateAll);

// =====================================================
// CALCULATION FUNCTIONS
// =====================================================

function calculateAll() {
    // Helper to get sanitized value
    const getVal = (id) => {
        const el = document.getElementById(id);
        if (!el) return 0;
        let v = el.value.trim().replace(/,/g, ''); // Remove commas
        
        // Handle range for quantity
        if (id === "quantityMeters" && v.includes("-")) {
            let parts = v.split("-");
            if (parts.length === 2) {
                let start = parseFloat(parts[0].trim());
                let end = parseFloat(parts[1].trim());
                if (!isNaN(start) && !isNaN(end)) {
                    return (start + end) / 2;
                }
            }
        }
        
        return parseFloat(v || 0);
    };

    // Get values
    const jobRate = getVal("jobRate");
    const pick = getVal("pick");
    const quantityMeters = getVal("quantityMeters");
    const brokeragePercent = getVal("weaverBrokeragePercent");
    const brokeragePaisa = getVal("weaverBrokeragePaisa");

    // 1. Rate = ((Job Rate/100) * Pick)
    const rate = (jobRate / 100) * pick;
    document.getElementById("rate").value = rate.toFixed(2);

    // 2. Amount = (Rate * Quantity Mtr)
    const amount = rate * quantityMeters;
    document.getElementById("amount").value = amount.toFixed(2);

    // 3. Brokerage % Amt = Amount * (Brokerage %/100)
    const brokeragePercentAmt = amount * (brokeragePercent / 100);
    document.getElementById("brokeragePercentAmt").value = brokeragePercentAmt.toFixed(2);

    // 4. Brokerage Mtr Amt = Quantity Mtr * (Brokerage Paisa/100)
    const brokerageMtrAmt = quantityMeters * (brokeragePaisa / 100);
    document.getElementById("brokerageMtrAmt").value = brokerageMtrAmt.toFixed(2);
}

// Calculate on page load if editing
document.addEventListener("DOMContentLoaded", function() {
    // Set today's date
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    document.getElementById("contractDate").value = `${year}-${month}-${day}`;

    // Initial calculation if editing
    calculateAll();
});

// =====================================================
// FORM SUBMISSION
// =====================================================

form.addEventListener("submit", function (e) {
    e.preventDefault();

    submitBtn.disabled = true;
    loader.style.display = "flex";

    fetch("/gen_bill", {
        method: "POST",
        body: new URLSearchParams(new FormData(form))
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(text => {
                throw new Error(text || `HTTP Error ${res.status}`);
            });
        }
        return res.text();
    })
    .then(data => {
        loader.style.display = "none";
        submitBtn.disabled = false;
        
        if (data === "SUCCESS" || data.startsWith("SUCCESS:")) {
            // Get the userId and contractNo from form
            let userId = document.querySelector('input[name="userId"]')?.value;
            let contractNo = document.querySelector('input[name="contractNo"]')?.value;
            
            // If backend returned the ids
            if (data.startsWith("SUCCESS:")) {
                const parts = data.split(":");
                if (parts.length >= 3) {
                    userId = parts[1];
                    contractNo = parts[2];
                    
                    // populate hidden inputs if missing
                    if (document.querySelector('input[name="userId"]')) {
                        document.querySelector('input[name="userId"]').value = userId;
                    }
                    if (document.querySelector('input[name="contractNo"]')) {
                        document.querySelector('input[name="contractNo"]').value = contractNo;
                    }
                }
            }

            // Stay on the same contract page instead of redirecting
            if (userId && contractNo) {
                // Change URL to edit mode without reloading the page
                window.history.replaceState({}, '', `/gen-bill/edit/${userId}/${contractNo}`);
                
                // CALL THE GENERATION FUNCTION DIRECTLY WITHOUT INTERMEDIATE POPUP
                if (typeof generateContractImage === "function") {
                    generateContractImage();
                } else {
                    console.error("generateContractImage function not found");
                }
            } else {
                // If we still don't have IDs, something is wrong, but stay on page
                console.error("Could not find userId or contractNo after save");
                Swal.fire("Warning", "Contract saved, but could not identify the contract number for generation. Please check the 'Report' page.", "warning");
            }
        } else {
            Swal.fire("Error", "Failed to save contract: " + data, "error");
            console.error("Server response:", data);
        }
    })
    .catch(err => {
        loader.style.display = "none";
        submitBtn.disabled = false;
        console.error("Error:", err);
        Swal.fire("Error", "Error: " + err.message, "error");
    });
});

// =====================================================
// QUALITY AUTO-GENERATION
// =====================================================

const qualityInputs = ["qual_width", "qual_reed", "qual_pick", "qual_warp", "qual_weft", "qual_weave"];
qualityInputs.forEach(id => {
    const element = document.getElementById(id);
    if (element) {
        element.addEventListener("input", generateQualityName);
    }
});

function generateQualityName() {
    let width = document.getElementById("qual_width").value || "";
    let reed = document.getElementById("qual_reed").value || "";
    let pick = document.getElementById("qual_pick").value || "";
    let warp = document.getElementById("qual_warp").value || "";
    let weft = document.getElementById("qual_weft").value || "";
    let weave = document.getElementById("qual_weave").value || "";

    // Build quality name with available fields - don't require all fields
    let qualityParts = [];
    
    if (warp || weft) {
        qualityParts.push(`${warp || "?"}*${weft || "?"}`);
    }
    if (reed || pick) {
        qualityParts.push(`${reed || "?"}*${pick || "?"}`);
    }
    if (width) {
        qualityParts.push(`${width}"`);
    }
    if (weave) {
        qualityParts.push(weave);
    }
    
    if (qualityParts.length > 0) {
        let quality = qualityParts.join(" / ");
        document.getElementById("qual_name").value = quality;
        console.log("✓ Quality name generated:", quality);
    } else {
        document.getElementById("qual_name").value = "";
    }
}

// =====================================================
// MODAL HANDLERS: ADD WEAVER
// =====================================================

document.getElementById("addWeaverForm")?.addEventListener("submit", function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    const params = new URLSearchParams();
    params.append("name", formData.get("weaver_name"));
    params.append("phno", formData.get("weaver_phno"));
    params.append("type", "WEAVER");
    params.append("brokeragePercent", formData.get("weaver_brokerage_percent"));
    params.append("brokeragePaisa", formData.get("weaver_brokerage_paisa"));

    fetch("/api/add-weaver", {
        method: "POST",
        body: params
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) {
            Swal.fire("Error", data.error, "error");
        } else {
            // Add to weaver dropdown
            const option = document.createElement("option");
            option.value = data.name;
            option.text = data.name;
            document.getElementById("weaverSelect").appendChild(option);
            
            // Select the new weaver
            $("#weaverSelect").val(data.name).trigger("change");

            // Manually set brokerage values and recalculate
            document.getElementById("weaverBrokeragePercent").value = data.brokeragePercent || 0;
            document.getElementById("weaverBrokeragePaisa").value = data.brokeragePaisa || 0;
            calculateAll();
            
            // Close modal and reset form
            bootstrap.Modal.getInstance(document.getElementById("addWeaverModal")).hide();
            document.getElementById("addWeaverForm").reset();
            
            Swal.fire("Success", "Weaver added successfully", "success");
        }
    })
    .catch(err => {
        Swal.fire("Error", "Failed to add weaver: " + err.message, "error");
    });
});

// =====================================================
// MODAL HANDLERS: ADD TRADER
// =====================================================

document.getElementById("addTraderForm")?.addEventListener("submit", function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    const params = new URLSearchParams();
    params.append("name", formData.get("trader_name"));
    params.append("phno", formData.get("trader_phno"));
    params.append("type", "TRADER");
    // Brokerage params removed from form, will default to 0 on server

    fetch("/api/add-trader", {
        method: "POST",
        body: params
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) {
            Swal.fire("Error", data.error, "error");
        } else {
            // Add to trader dropdown
            const option = document.createElement("option");
            option.value = data.name;
            option.text = data.name;
            document.getElementById("traderSelect").appendChild(option);
            
            // Select the new trader
            $("#traderSelect").val(data.name).trigger("change");
            
            // Close modal and reset form
            bootstrap.Modal.getInstance(document.getElementById("addTraderModal")).hide();
            document.getElementById("addTraderForm").reset();
            
            Swal.fire("Success", "Trader added successfully", "success");
        }
    })
    .catch(err => {
        Swal.fire("Error", "Failed to add trader: " + err.message, "error");
    });
});

// =====================================================
// MODAL HANDLERS: ADD QUALITY
// =====================================================

document.getElementById("addQualityForm")?.addEventListener("submit", function(e) {
    e.preventDefault();
    
    // Validate quality name is not empty
    const qualityName = document.getElementById("qual_name").value.trim();
    if (!qualityName) {
        Swal.fire("Error", "Please fill in the quality details. Quality name cannot be empty.", "error");
        console.error("✗ Quality name is empty. Please fill in at least some fields.");
        return;
    }
    
    const formData = new FormData(this);
    const params = new URLSearchParams();
    params.append("qualityName", formData.get("quality_name"));
    params.append("pick", formData.get("quality_pick") || "");
    params.append("width", formData.get("quality_width") || "");
    params.append("reed", formData.get("quality_reed") || "");
    params.append("warp", formData.get("quality_warp") || "");
    params.append("weft", formData.get("quality_weft") || "");
    params.append("weave", formData.get("quality_weave") || "");

    console.log("✓ Submitting quality:", Object.fromEntries(params));
    
    fetch("/api/add-quality", {
        method: "POST",
        body: params
    })
    .then(res => {
        console.log("Response status:", res.status);
        if (!res.ok) {
            return res.text().then(text => {
                throw new Error(`HTTP ${res.status}: ${text}`);
            });
        }
        return res.json();
    })
    .then(data => {
        if (data.error) {
            console.error("✗ API Error:", data.error);
            Swal.fire("Error", data.error, "error");
        } else {
            console.log("✓ Quality added successfully:", data);
            // Add to quality dropdown
            const option = document.createElement("option");
            option.value = data.qualityName;
            option.text = data.qualityName;
            document.getElementById("qualitySelect").appendChild(option);
            
            // Select the new quality
            $("#qualitySelect").val(data.qualityName).trigger("change");
            
            // Explicitly set the pick value as well, as trigger('change') might not 
            // trigger our custom select2:select handler which fetch from API
            if (data.pick) {
                document.getElementById("pick").value = data.pick;
                console.log("✓ Pick value set from newly added quality:", data.pick);
                calculateAll();
            }
            
            // Close modal and reset form
            const modalElement = document.getElementById("addQualityModal");
            const modal = bootstrap.Modal.getInstance(modalElement);
            if (modal) modal.hide();
            document.getElementById("addQualityForm").reset();
            
            Swal.fire("Success", "Quality added successfully", "success");
        }
    })
    .catch(err => {
        console.error("✗ Error adding quality:", err);
        Swal.fire("Error", "Failed to add quality: " + err.message, "error");
    });
});

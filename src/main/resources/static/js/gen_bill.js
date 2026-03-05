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
const submitBtn = document.getElementById("submitBtn");

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
            const url = `/api/quality-details/${encodeURIComponent(qualityName)}`;
            console.log("Fetching from:", url);
            fetch(url)
                .then(res => {
                    console.log("Response status:", res.status);
                    return res.json();
                })
                .then(data => {
                    console.log("Quality data:", data);
                    document.getElementById("pick").value = data.pick || 0;
                    calculateAll();
                })
                .catch(err => console.error("Error fetching quality details:", err));
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
    // Get values
    const jobRate = parseFloat(document.getElementById("jobRate")?.value || 0);
    const pick = parseFloat(document.getElementById("pick")?.value || 0);
    const quantityMeters = parseFloat(document.getElementById("quantityMeters")?.value || 0);
    const brokeragePercent = parseFloat(document.getElementById("weaverBrokeragePercent")?.value || 0);
    const brokeragePaisa = parseFloat(document.getElementById("weaverBrokeragePaisa")?.value || 0);

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
    .then(() => {
        loader.style.display = "none";
        submitBtn.disabled = false;
        Swal.fire("Success", "Job contract saved successfully", "success")
            .then(() => window.location.href = "/dashboard");
    })
    .catch(() => {
        loader.style.display = "none";
        submitBtn.disabled = false;
        Swal.fire("Error", "Server error occurred", "error");
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

    if (warp && weft && reed && pick && width) {
        let quality = `${warp}*${weft} / ${reed} * ${pick} / ${width}" - ${weave}`;
        document.getElementById("qual_name").value = quality;
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
    params.append("brokeragePercent", formData.get("trader_brokerage_percent"));
    params.append("brokeragePaisa", formData.get("trader_brokerage_paisa"));

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
    const formData = new FormData(this);
    const params = new URLSearchParams();
    params.append("qualityName", formData.get("quality_name"));
    params.append("pick", formData.get("quality_pick"));
    params.append("width", formData.get("quality_width"));
    params.append("reed", formData.get("quality_reed"));
    params.append("warp", formData.get("quality_warp"));
    params.append("weft", formData.get("quality_weft"));

    fetch("/api/add-quality", {
        method: "POST",
        body: params
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) {
            Swal.fire("Error", data.error, "error");
        } else {
            // Add to quality dropdown
            const option = document.createElement("option");
            option.value = data.qualityName;
            option.text = data.qualityName;
            document.getElementById("qualitySelect").appendChild(option);
            
            // Select the new quality
            $("#qualitySelect").val(data.qualityName).trigger("change");
            
            // Close modal and reset form
            bootstrap.Modal.getInstance(document.getElementById("addQualityModal")).hide();
            document.getElementById("addQualityForm").reset();
            
            Swal.fire("Success", "Quality added successfully", "success");
        }
    })
    .catch(err => {
        Swal.fire("Error", "Failed to add quality: " + err.message, "error");
    });
});

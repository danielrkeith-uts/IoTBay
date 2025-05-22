function handleStaffCheckbox() {
    var checkBox = document.getElementById("staff-checkbox");

    var staffSection = document.getElementById("staff-section");

    const customerTypeSection = document.getElementById("customer-type-section");

    staffSection.style.display = checkBox.checked ? "block" : "none";
    customerTypeSection.style.display = staffCheckbox.checked ? "none" : "block";

}

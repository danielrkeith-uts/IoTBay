function handleStaffCheckbox() {
    var checkBox = document.getElementById("staff-checkbox");

    var staffSection = document.getElementById("staff-section");

    staffSection.style.display = checkBox.checked ? "block" : "none";
}

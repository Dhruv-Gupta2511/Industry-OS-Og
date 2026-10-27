import os

print("Applying null-safety fixes...")

# 1. OperatorService.java
op_path = "industry-os/src/main/java/com/industryos/service/OperatorService.java"
if os.path.exists(op_path):
    with open(op_path, "r", encoding="utf-8") as f:
        code = f.read()

    code = code.replace(
        "partUsageMap.put(pId, partUsageMap.getOrDefault(pId, 0) + usage.getQtyUsed());",
        "int qUsed = usage.getQtyUsed() != null ? usage.getQtyUsed() : 0;\n                partUsageMap.put(pId, partUsageMap.getOrDefault(pId, 0) + qUsed);"
    )

    old_stock_check = """            if (part.getStockQty() <= 0) {
                partDTO.setStockStatus("OUT_OF_STOCK");
                partDTO.setReorderRecommended(true);
            } else if (part.getStockQty() <= part.getReorderLevel()) {"""

    new_stock_check = """            int stock = part.getStockQty() != null ? part.getStockQty() : 0;
            int reorder = part.getReorderLevel() != null ? part.getReorderLevel() : 0;
            if (stock <= 0) {
                partDTO.setStockStatus("OUT_OF_STOCK");
                partDTO.setReorderRecommended(true);
            } else if (stock <= reorder) {"""

    if old_stock_check in code:
        code = code.replace(old_stock_check, new_stock_check)
        print("Updated OperatorService stock checks.")
    else:
        print("Note: old_stock_check not matched by exact string, checking alternate")
        code = code.replace("if (part.getStockQty() <= 0)", "int stock = part.getStockQty() != null ? part.getStockQty() : 0; int reorder = part.getReorderLevel() != null ? part.getReorderLevel() : 0; if (stock <= 0)")
        code = code.replace("else if (part.getStockQty() <= part.getReorderLevel())", "else if (stock <= reorder)")

    with open(op_path, "w", encoding="utf-8") as f:
        f.write(code)
    print("OperatorService.java updated.")

# 2. ExecutiveService.java
ex_path = "industry-os/src/main/java/com/industryos/service/ExecutiveService.java"
if os.path.exists(ex_path):
    with open(ex_path, "r", encoding="utf-8") as f:
        code = f.read()

    code = code.replace(".map(Payment::getAmount)", ".map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)")
    code = code.replace(".map(Department::getBudget)", ".map(d -> d.getBudget() != null ? d.getBudget() : BigDecimal.ZERO)")
    code = code.replace(".map(MaintenanceLog::getLabourCost)", ".map(m -> m.getLabourCost() != null ? m.getLabourCost() : BigDecimal.ZERO)")
    code = code.replace(".map(Vehicle::getCapacityTons)", ".map(v -> v.getCapacityTons() != null ? v.getCapacityTons() : BigDecimal.ZERO)")
    code = code.replace(".map(EnergyConsumption::getUnitsKwh)", ".map(e -> e.getUnitsKwh() != null ? e.getUnitsKwh() : BigDecimal.ZERO)")
    code = code.replace(".map(EnergyConsumption::getCost)", ".map(e -> e.getCost() != null ? e.getCost() : BigDecimal.ZERO)")

    with open(ex_path, "w", encoding="utf-8") as f:
        f.write(code)
    print("ExecutiveService.java updated with null-safe stream reductions.")

# 3. DeptHeadService.java
dh_path = "industry-os/src/main/java/com/industryos/service/DeptHeadService.java"
if os.path.exists(dh_path):
    with open(dh_path, "r", encoding="utf-8") as f:
        code = f.read()

    code = code.replace(
        "good += b.getGoodQty();",
        "good += (b.getGoodQty() != null ? b.getGoodQty() : 0);"
    )
    with open(dh_path, "w", encoding="utf-8") as f:
        f.write(code)
    print("DeptHeadService.java updated with null-safe batch quantity summation.")

print("All null-safety hotfixes applied.")


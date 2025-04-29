import { useEffect, useState } from "react";
import { AgGridReact } from "ag-grid-react";
import { ModuleRegistry } from "ag-grid-community";
import { ClientSideRowModelModule } from "ag-grid-community";
import { Typography } from "@mui/material";

ModuleRegistry.registerModules([ClientSideRowModelModule]);

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [colDefs, setColDefs] = useState([
    { field: "name" },
    { field: "description" },
  ]);
  const [rowData, setRowData] = useState([]);
  const defaultColDef = {
    flex: 1,
  };

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch("http://localhost:8080/api/categories");
      setCategories(await response.json());
    };
    fetchData();
  }, []);

  useEffect(() => {
    if (categories.length > 0) {
      const rows = categories.map((category) => ({
        name: category.name,
        description: category.description,
      }));
      setRowData(rows);
    }
  }, [categories]);

  return (
    <>
      <Typography variant="h5">Categories</Typography>
      <div style={{ height: 300, width: "100%", marginTop: 10 }}>
        <AgGridReact
          rowData={rowData}
          columnDefs={colDefs}
          defaultColDef={{ flex: 1, resizable: true }}
        />
      </div>
    </>
  );
}

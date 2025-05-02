import { AgGridReact } from "ag-grid-react";
import { ModuleRegistry } from "ag-grid-community";
import { ClientSideRowModelModule } from "ag-grid-community";
import { Typography, Box } from "@mui/material";
import useFetchData from "../hooks/useFetchData";

ModuleRegistry.registerModules([ClientSideRowModelModule]);

export default function Categories() {
  const { data: categories } = useFetchData(
    import.meta.env.VITE_API_URL + "categories"
  );

  console.log(categories);

  const colDefs = [
    {
      headerName: "Category Name",
      sortable: true,
      filter: "agTextColumnFilter",
      field: "name",
      cellRenderer: (param) => {
        return (
          <a
            href={`/categories/${param.data.id}`}
            style={{ textDecoration: "underline", color: "#1976d2" }}
          >
            {param.value}
          </a>
        );
      },
    },
    {
      headerName: "Category Description",
      field: "description",
      sortable: true,
      filter: "agTextColumnFilter",
    },
  ];

  return (
    <Box sx={{ padding: 3 }}>
      <Typography
        variant="h4"
        sx={{ fontWeight: "bold", color: "#1976d2", letterSpacing: "1px" }}
      >
        Categories
      </Typography>
      <div
        className="ag-theme-alpine"
        style={{ height: 300, width: "100%", marginTop: 10 }}
      >
        <AgGridReact
          rowData={categories}
          columnDefs={colDefs}
          defaultColDef={{ flex: 1, resizable: true }}
          suppressCellFocus={true}
        />
      </div>
    </Box>
  );
}

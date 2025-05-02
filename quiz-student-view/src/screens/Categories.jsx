import { AgGridReact } from "ag-grid-react";
import { ModuleRegistry } from "ag-grid-community";
import { ClientSideRowModelModule } from "ag-grid-community";
import { Typography } from "@mui/material";
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
      field: "name",
      cellRenderer: (param) => {
        return <a href={`/categories/${param.data.id}`}>{param.value}</a>;
      },
    },
    { headerName: "Category Description", field: "description" },
  ];

  return (
    <>
      <Typography variant="h5">Categories</Typography>
      <div style={{ height: 300, width: "100%", marginTop: 10 }}>
        <AgGridReact
          rowData={categories}
          columnDefs={colDefs}
          defaultColDef={{ flex: 1, resizable: true }}
          suppressCellFocus={true}
        />
      </div>
    </>
  );
}
